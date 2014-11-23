package uk.co.bbc.fabric.interfaces.services;

import java.util.Calendar;
import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.common.IEntityStatus;
import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDao;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDao;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Service
public class EpisodeProcessor extends BaseEntityProcessor<EpisodeView,String,String> {

	private MasterEpisodeProcessor masterEpisodeProcessor;

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

	public MasterEpisodeProcessor getMasterEpisodeProcessor() {
		return masterEpisodeProcessor;
	}

	@Autowired
	public void setMasterEpisodeProcessor(
			MasterEpisodeProcessor masterEpisodeProcessor) {
		this.masterEpisodeProcessor = masterEpisodeProcessor;
	}

	public OnAirEntityDao getAirEntityDao() {
		return airEntityDao;
	}

	@Autowired
	public void setAirEntityDao(OnAirEntityDao airEntityDao) {
		this.airEntityDao = airEntityDao;
	}

	//mark episode TO_PROCESS in local n onair
	@Override
	public void process(EpisodeView entity) throws OnairUpdateException{
		Episode dEpisode = derbyEntityDao.findEpisodeByID(entity.getOnairEntityId().toString());
		
		if (dEpisode != null && null ==dEpisode.getEntityStatus()) {
			dEpisode.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			dEpisode.setEpisodeReleased("Y");
			dEpisode.setReleaseTime(Calendar.getInstance().getTime());
			derbyEntityDao.updateEntity(dEpisode);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),dEpisode);
			}
			catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}
	}

	public String handle(String coreNumber_VersionNumber) throws OnairUpdateException{
		
		
		if(!coreNumber_VersionNumber.contains("/")){
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}
		
		String masterEpisodeStatus = null;
		String[] coreVersions = coreNumber_VersionNumber.split("/");
		String coreNumber = coreVersions[0];
		String versionNumber = coreVersions[1];
		String uid=coreNumber+"/"+versionNumber;
		EpisodeView episode = new EpisodeView();
		episode.setCoreNumber(coreNumber);
		episode.setVersionNumber(versionNumber);
		//List<Episode> episodes =derbyEntityDao.findEpisodeByUid(uid);
		//Retrieve episode from on-air and copy it in local table after filtering 
		//filter logic : all EPs after a Failed/Parked EP.
		//insert entity status as null in local db
		derbyEntityDao.loadToBeProcessedEpisodes(airEntityDao.findEpisodeByCoreNumberVersionNumber(episode));
		List<Episode> episodes=derbyEntityDao.findEpisodeByUid(coreNumber_VersionNumber);
		if (episodes ==null || episodes.isEmpty()){
			//orphan pe
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}else if(!Utility.isEntityValid(episodes)){
			//We have got at least one failed EP means we will discard the list.
			derbyEntityDao.updateEntity(episodes.get(0));
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}
		else if (Utility.isAllCompleted(episodes)) {
			return IEntityStatus.ENTITY_STATUS_COMPLETED;
		} else {
			if(episodes!=null && !episodes.isEmpty()){
				for(Episode ep:episodes){
					episode = airEntityDao.findByEpisodeOnAirEntityID(ep.getOnairEntityId().toString());
					System.out.println("Starting with : "+ep.getOnairentityid());
					if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(ep.getEntityStatus())){
						continue;
					}
					else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(ep.getEntityStatus())){
						System.out.println("Waiting for EP : " +ep.getOnairentityid()+" to be completed.");
						return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
					}
					
					
					if(Utility.isMaster(episode)){
						//current episode is master hence processing directly.
						masterEpisodeStatus =masterEpisodeProcessor.processMasterEpisodeDirectlyAndGetStatus(episode);
					}else{
						//process master episode
						masterEpisodeStatus = masterEpisodeProcessor.processAndGetStatus(episode);
					}
					System.out.println("Master Episode Status :"+masterEpisodeStatus);
					if (IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(masterEpisodeStatus) && !Utility.isMaster(episode)) {
						System.out.println("Episode is getting processed "+ep.getOnairEntityId());
						process(episode);
						return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
					} else if (IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(masterEpisodeStatus)) {
						if(ep!=null){
							ep.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
							derbyEntityDao.updateEntity(ep);
						}
						return IEntityStatus.ENTITY_STATUS_FAILED;
					}
					else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(masterEpisodeStatus)){
						System.out.println("Waiting for ME : " +episode.getOnairEntityId()+" to be completed.");
						return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
					}
				}
			}
		}
		return masterEpisodeStatus;
	}


	public String processStandaloneEpisodes() throws OnairUpdateException{
		
		
		List<Episode> standaloneEpisodes=derbyEntityDao.getStandAloneEpisodes();
		if(standaloneEpisodes==null || standaloneEpisodes.isEmpty()){
			List<EpisodeView> standaloneEpisodeViews =airEntityDao.getStandaloneEpisodeUIDs();
			derbyEntityDao.loadToBeProcessedEpisodes(standaloneEpisodeViews);
			standaloneEpisodes = derbyEntityDao.getStandAloneEpisodes();
		}
		if(Utility.isDryRun() && Utility.isAllCompleted(standaloneEpisodes)){
			System.out.println(" Dry run Finished, all Standalone EPs Completed ");
		}
		
		if(standaloneEpisodes!=null && !standaloneEpisodes.isEmpty()){
			for(Episode episode:standaloneEpisodes){
				String uid = episode.getUid();
				String status = handle(uid);
				if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(status)){
					return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
				}
				else if(IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(status)){
					derbyEntityDao.updateEpisodesAsFailed(uid);
					continue;
				}
				else if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(status)){
					continue;
				}
			}
		}
		return null;
		
	}
	//loop through each ep from local table and retrieve corresponding data from on-air and update local table.
	@Override
	public void refresh() {
		List<Episode> episodes = derbyEntityDao.getReleasedEpisodes();
		if(episodes!=null && !episodes.isEmpty()){
			for(Episode episode:episodes){
				OnAirEntity entity= airEntityDao.findOnAirEntityByID(episode.getOnairEntityId().toString());
				if(Utility.isDryRun()){
					if(Utility.isFailed(entity)){
						episode.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
					}else{
						episode.setEntitystatus(IEntityStatus.ENTITY_STATUS_COMPLETED);
					}
					episode.setLastprocessed(Utility.getCurrentTimeInGMT());
					episode.setDryRun("DR");
					derbyEntityDao.updateEntity(episode);
					if(Utility.isAllCompleted(derbyEntityDao.findEpisodeByUid(episode.getUid()))){
						derbyEntityDao.updatePEAsEpisodeComplete(episode.getUid());
					}
					System.out.println("Refreshed Episode: "+episode.getOnairentityid());
				}
				else if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(entity.getEntityStatus())
						||IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(entity.getEntityStatus())
						||IEntityStatus.ENTITY_STATUS_PARKED.equalsIgnoreCase(entity.getEntityStatus())){
					episode.setEntitystatus(entity.getEntityStatus());
					episode.setLastprocessed(entity.getLastProcessed());
					derbyEntityDao.updateEntity(episode);
					if(Utility.isAllCompleted(derbyEntityDao.findEpisodeByUid(episode.getUid()))){
						derbyEntityDao.updatePEAsEpisodeComplete(episode.getUid());
					}
					System.out.println("Refreshed Episode: "+episode.getOnairentityid());
				}
			}
		}
		
	}
}
