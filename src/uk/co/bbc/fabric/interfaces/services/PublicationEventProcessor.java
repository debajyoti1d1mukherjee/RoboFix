package uk.co.bbc.fabric.interfaces.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.common.IEntityStatus;
import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDao;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDao;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Service
public class PublicationEventProcessor extends BaseEntityProcessor<PublicationEventView,String,String> {

	
	
	private OnAirEntityDao airEntityDao;
	
	private OnAirDerbyDao derbyEntityDao;
	
	public OnAirDerbyDao getDerbyDao() {
		return derbyEntityDao;
	}
	@Autowired
	@Qualifier("derby")
	public void setDerbyDao(OnAirDerbyDao derbyEntityDao) {
		this.derbyEntityDao = derbyEntityDao;
	}
	public OnAirEntityDao getAirEntityDao() {
		return airEntityDao;
	}
	@Autowired
	public void setAirEntityDao(OnAirEntityDao airEntityDao) {
		this.airEntityDao = airEntityDao;
	}

	private EpisodeProcessor episodeProcessor;
	//Mark the PE as TO_PROCESS both in on-air and local
	@Override
	public void process(PublicationEventView entity) throws OnairUpdateException{
		
		Publicationevent pe =  derbyEntityDao.findPEByID(entity.getOnairEntityId().toString());
		if(pe !=null){
			pe.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			pe.setReleaseTime(Utility.getCurrentTimeInGMT());
			pe.setPvreleased("Y");
			derbyEntityDao.updateEntity(pe);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),pe);
			}
			catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}
	}
	
	
	public String handle(String str)throws OnairUpdateException{
		
		//retrieve all PEs from local table where processedstate is null.
		List<Publicationevent> listPv = derbyEntityDao.getPublicationEvents();
		//first time run or when all PEs in local table are processed, copy data from on-air to local table.
		if(listPv ==null || listPv.isEmpty()){
			List<PublicationEventView> list = airEntityDao.getPublicationEventsForProcessing();
			listPv =derbyEntityDao.loadToBeProcessedPublicationEvents(list);
			Collections.sort(listPv);
			System.out.println("Successfully retrieved temp db :" +listPv.size());
		}
		if(listPv !=null && !listPv.isEmpty()){
			for(int idxPv=0;idxPv<listPv.size();idxPv++){
				Publicationevent publicationEvent=listPv.get(idxPv);
				System.out.println(" PE : "+publicationEvent.getUid());
				//A PE marked TO_PROCESS in on-air in previous schedule and not yet processed.So wait till it completes.
				if("Y".equalsIgnoreCase(publicationEvent.getPvreleased())&& publicationEvent.getProcessedstate()==null){
					System.out.println("Waiting for PE : " +publicationEvent.getOnairentityid()+" to be completed.");
					break;
				}
				
				//A PE where episode is complete.mark that PE as TO_PROCESS in on-air and local.
				if("Y".equalsIgnoreCase(publicationEvent.getEpisodecompleted()) && "N".equalsIgnoreCase(publicationEvent.getPvreleased())){
					PublicationEventView publicationEventView = (PublicationEventView)airEntityDao.findPublicationEventByEntityID(publicationEvent.getOnairEntityId().toString());
					System.out.println("Publication event is getting processed : "+publicationEvent.getOnairentityid());
					processEnitity(publicationEventView);
					break;
				}
				//A PE where episode is not complete. Go up in the tree.
				else if("N".equalsIgnoreCase(publicationEvent.getEpisodecompleted()) && "N".equalsIgnoreCase(publicationEvent.getPvreleased())){
					PublicationEventView publicationEventView = (PublicationEventView)airEntityDao.findPublicationEventByEntityID(publicationEvent.getOnairEntityId().toString());
					if(publicationEventView==null || publicationEventView.getUid()==null ){
						publicationEvent.setProcessedstate(IEntityStatus.ENTITY_STATUS_FAILED);
						derbyEntityDao.updateEntity(publicationEvent);
						continue;
					}
					//process episodes
					String episodeStatus=episodeProcessor.processAndGetStatus(publicationEvent.getUid());
					System.out.println("Episode status : "+episodeStatus);
					if(IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(episodeStatus)){
						publicationEvent.setProcessedstate(IEntityStatus.ENTITY_STATUS_FAILED);
						publicationEvent.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
						derbyEntityDao.updateEntity(publicationEvent);
						continue;
					}
					else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(episodeStatus)){
						break;
					}
					else if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(episodeStatus)){
						System.out.println("Publication event is getting processed : "+publicationEvent.getOnairentityid());
						processEnitity(publicationEventView);
						break;
					}
				}
			}
		}
		else{
			
			episodeProcessor.processStandaloneEpisodes();
		}
		return new String();
	}
		
	

	public EpisodeProcessor getEpisodeProcessor() {
		return episodeProcessor;
	}
	@Autowired
	public void setEpisodeProcessor(EpisodeProcessor episodeProcessor) {
		this.episodeProcessor = episodeProcessor;
	}
	
	//loop through each pe from local table and retrieve corresponding data from on-air and update local table.
	@Override
	public void refresh() {
		List<Publicationevent> pubEvents =derbyEntityDao.getReleasedPublicationEvents();
		if(pubEvents!=null && !pubEvents.isEmpty()){
			for(Publicationevent pe:pubEvents){
				OnAirEntity entity= airEntityDao.findOnAirEntityByID(pe.getOnairEntityId().toString());
				if(Utility.isDryRun()){
					if(Utility.isFailed(entity)){
						pe.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
					}else{
						pe.setEntitystatus(IEntityStatus.ENTITY_STATUS_COMPLETED);
					}
					pe.setEntitystatus(IEntityStatus.ENTITY_STATUS_COMPLETED);
					pe.setProcessedstate(IEntityStatus.ENTITY_STATUS_COMPLETED);
					pe.setLastprocessed(Utility.getCurrentTimeInGMT());
					pe.setDryRun("DR");
					derbyEntityDao.updateEntity(pe);
					System.out.println("Refreshed PE: "+pe.getOnairentityid());
				}
				else if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(entity.getEntityStatus())
						||IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(entity.getEntityStatus())
						||IEntityStatus.ENTITY_STATUS_PARKED.equalsIgnoreCase(entity.getEntityStatus())){
					pe.setEntitystatus(entity.getEntityStatus());
					pe.setProcessedstate(entity.getEntityStatus());
					pe.setLastprocessed(entity.getLastProcessed());
					derbyEntityDao.updateEntity(pe);
					System.out.println("Refreshed PE: "+pe.getOnairentityid());
				}
			}
		}
		
	}

}
