package uk.co.bbc.fabric.interfaces.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.common.IEntityStatus;
import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDao;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDao;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Service
public class MasterEpisodeProcessor extends BaseEntityProcessor<EpisodeView,EpisodeView,String> {
	
	private SeriesProcessor seriesProcessor;
	
	public SeriesProcessor getSeriesProcessor() {
		return seriesProcessor;
	}
	@Autowired
	public void setSeriesProcessor(SeriesProcessor seriesProcessor) {
		this.seriesProcessor = seriesProcessor;
	}
	
	private OnAirDerbyDao derbyEntityDao;
	
	public OnAirDerbyDao getDerbyDao() {
		return derbyEntityDao;
	}
	@Autowired
	@Qualifier("derby")
	public void setDerbyDao(OnAirDerbyDao derbyEntityDao) {
		this.derbyEntityDao = derbyEntityDao;
	}
	
	private OnAirEntityDao airEntityDao;
	public OnAirEntityDao getAirEntityDao() {
		return airEntityDao;
	}
	@Autowired
	public void setAirEntityDao(OnAirEntityDao airEntityDao) {
		this.airEntityDao = airEntityDao;
	}
		
	@Override
	public void process(EpisodeView entity) throws OnairUpdateException{
		
		Episode dEpisode=derbyEntityDao.findEpisodeByID(entity.getOnairEntityId().toString());
		
		if(dEpisode==null){
			Episode dEpisode1= new Episode();
			Utility.transferEpisodeAttribute(entity, dEpisode1);
			dEpisode1.setType("M");
			dEpisode1.setEpisodeReleased("Y");
			dEpisode1.setReleaseTime(Utility.getCurrentTimeInGMT());
			dEpisode1.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			derbyEntityDao.insertEpisode(dEpisode1);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),dEpisode1);
			}catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}
		else if(!IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(dEpisode.getEntityStatus())){
			dEpisode.setType("M");
			dEpisode.setEpisodeReleased("Y");
			dEpisode.setReleaseTime(Utility.getCurrentTimeInGMT());
			dEpisode.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			derbyEntityDao.updateEntity(dEpisode);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),dEpisode);
			}catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}
	}
	public String handle(EpisodeView episode)throws OnairUpdateException{
				
		String seriesStatus=null;
		EpisodeView masterEpisode=new EpisodeView();
		String masterEpisodeId= episode.getCanonicalVersionRefEpisodeId().toString();
		System.out.println("Passed in Episode id "+episode.getOnairEntityId());
		System.out.println("Master Episode id "+masterEpisodeId);
		masterEpisode.setOnairEpisodeId(new BigDecimal(masterEpisodeId));
		masterEpisode=airEntityDao.findByEpisodeID(masterEpisode);
		if(masterEpisode == null){
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}
		String uid = masterEpisode.getCoreNumber()+"/"+masterEpisode.getVersionNumber();
		derbyEntityDao.loadToBeProcessedEpisodes(airEntityDao.findEpisodeByCoreNumberVersionNumber(masterEpisode));
		List<Episode> masterEpisodes=derbyEntityDao.findEpisodeByUid(masterEpisode.getCoreNumber()+"/"+masterEpisode.getVersionNumber());
		if(masterEpisodes ==null || masterEpisodes.isEmpty()){
			return IEntityStatus.ENTITY_STATUS_FAILED;
		} else if(!Utility.isEntityValid(masterEpisodes)){
			derbyEntityDao.updateEntity(masterEpisodes.get(0));
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}
		else if(Utility.isAllCompleted(masterEpisodes)){
			return IEntityStatus.ENTITY_STATUS_COMPLETED;
		} else{
			
			if(masterEpisodes!=null && !masterEpisodes.isEmpty()){
				for(Episode ep:masterEpisodes){
					//releasing all the relevant MEs one by one
					masterEpisode = airEntityDao.findByEpisodeOnAirEntityID(ep.getOnairEntityId().toString());
					System.out.println("Starting with : "+ep.getOnairentityid());
					if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(ep.getEntityStatus())){
						continue;
					}
					else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(ep.getEntityStatus())){
						System.out.println("Waiting for ME : " +ep.getOnairEntityId()+" to be completed.");
						return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
					}
					seriesStatus =  seriesProcessor.processAndGetStatus(episode.getSeriesId());
					System.out.println("Series Status :"+seriesStatus);
					if (IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(seriesStatus)) {
						System.out.println("Master episode is getting processed "+masterEpisode.getOnairEntityId());
						process(masterEpisode);
						return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
					} else if (IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(seriesStatus)) {
						ep.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
						derbyEntityDao.updateEntity(ep);
						return IEntityStatus.ENTITY_STATUS_FAILED;
					}
					else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(seriesStatus)){
						System.out.println("Waiting for SE : " +episode.getSeriesId()+"(Series ID) to be completed.");
						break;
					}
				}
			}
		}
		return seriesStatus;
	}
	
	public String processMasterEpisodeDirectlyAndGetStatus(EpisodeView episode)throws OnairUpdateException{
		
		String seriesStatus =  seriesProcessor.processAndGetStatus(episode.getSeriesId());
		if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(seriesStatus)){
			System.out.println("Master episode is getting processed "+episode.getOnairEntityId());
			process(episode);
			return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
		} 
		return seriesStatus;
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	
}
