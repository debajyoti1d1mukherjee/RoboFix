package uk.co.bbc.fabric.interfaces.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.dao.hibernate.LockManager;
import uk.co.bbc.fabric.interfaces.exception.LockException;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Service
public class EntityHandler implements IEntityHandler {
	
	
	private PublicationEventProcessor publicationEventProcessor;
	private EpisodeProcessor episodeProcessor;	 
	private String connectionTimeOut;
	
	/*@Autowired
	public EntityHandler(EpisodeProcessor episodeProcessor, PublicationEventProcessor publicationEventProcessor){
		this.publicationEventProcessor = publicationEventProcessor;
		this.episodeProcessor = episodeProcessor;
	}*/

	public String getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(String connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}
	/*
	 * refreshes local tables and starts processing
	 */
	public void startProcessing() throws OnairUpdateException,LockException{
		
		//getting the lock to prevent two simultaneous schedule process at the same time.
		// Lock table has been used to implement this.
		boolean lockObtained=LockManager.lock();
		if(lockObtained){
			refreshTempTables();
			checkStateAndProcess();
			boolean lockReleased = LockManager.unlock();
			if(!lockReleased){
				throw new LockException("Unlocking Failed");
			}
		}else{
			throw new LockException("Waiting for lock");
		}

	}
	  
	public void checkStateAndProcess()throws OnairUpdateException{
		//publicationEventProcessor.startPublicationEventProcessing();
		publicationEventProcessor.processAndGetStatus(new String());
	}
	/*
	 * refreshes the episode and publication-event tables to sync on-air tables with local tables.
	 */
	public void refreshTempTables(){
		episodeProcessor.refreshTempTables();
		publicationEventProcessor.refreshTempTables();
	}

	
	public PublicationEventProcessor getPublicationEventProcessor() {
		return publicationEventProcessor;
	}

	@Autowired
	public void setPublicationEventProcessor(
			PublicationEventProcessor publicationEventProcessor) {
		this.publicationEventProcessor = publicationEventProcessor;
	}

	public EpisodeProcessor getEpisodeProcessor() {
		return episodeProcessor;
	}
    
	@Autowired
	public void setEpisodeProcessor(EpisodeProcessor episodeProcessor) {
		this.episodeProcessor = episodeProcessor;
	}

}
