package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import uk.co.bbc.fabric.interfaces.common.IEntityStatus;
import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;

public class OnAirDerbyDaoImpl extends HibernateDaoSupport implements OnAirDerbyDao{
	
	@Resource(name="myConfigProperties")
	private Properties myProperties;
	
	public static String connectionURL=null;
	public static String driver=null;
	public static String isDryRun=null;
	
	static String errorMsg =null;
	 @PostConstruct
	  public void init() {
		 driver=myProperties.getProperty("derby.dataSource.driverClass");
		 connectionURL=myProperties.getProperty("derby.dataSource.jdbcURL")+"/"+myProperties.getProperty("derby.dataSource.database");
		 isDryRun=myProperties.getProperty("dryRun");
	 }

	public Episode findEpisodeByID(String onairEntityID) {
		String hql = "from Episode where onairentityid=" + onairEntityID;
		System.out.println(" HQL : " + hql);
		List<Episode> list = (List<Episode>) getHibernateTemplate().find(hql);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public Publicationevent findPEByID(String onairEntityID) {
		String hql = "from Publicationevent where onairentityid=" + onairEntityID;
		List<Publicationevent> list = (List<Publicationevent>) getHibernateTemplate().find(hql);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public void insertEpisode(BaseEntity dEpisode) {
		getHibernateTemplate().save(dEpisode);

	}

	public void updateEntity(BaseEntity dEpisode) {
		
		if(dEpisode instanceof Publicationevent && IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(dEpisode.getEntityStatus())){
			Publicationevent pe=(Publicationevent)dEpisode;
			String uid=pe.getUid();
			String hql = "update Publicationevent set processedstate='FAILED' , entitystatus='FAILED' where uid='"+uid+"'";
			getHibernateTemplate().bulkUpdate(hql);
		}
		else if(dEpisode instanceof Episode && IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(dEpisode.getEntityStatus())){
			String uid = ((Episode)dEpisode).getUid();
			String hql = "update Episode set entitystatus='FAILED' where uid='"+uid+"'";
			getHibernateTemplate().bulkUpdate(hql);
		}
		else{
			getHibernateTemplate().update(dEpisode);
		}

	}
	
	public void updateEpisodesAsFailed(String uid) {
		Episode episode = new Episode();
		episode.setUid(uid);
		episode.setEntitystatus(IEntityStatus.ENTITY_STATUS_FAILED);
		updateEntity(episode);
	}
/*
 * copy data from onair to local publicationevent.
 * 
 */
	
	public List<Publicationevent> loadToBeProcessedPublicationEvents(
			List<PublicationEventView> publicationEventsForProcessing) {
		List<Publicationevent> listPE = new ArrayList<Publicationevent>();
		Publicationevent tempPE = null;
		for (PublicationEventView pe : publicationEventsForProcessing) {
			tempPE = new Publicationevent();
			Utility.transferPublicationEventAttribute(pe, tempPE);
			tempPE.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			if(findPEByID(tempPE.getOnairEntityId().toString())==null){
				listPE.add(tempPE);
			}
		}
		getHibernateTemplate().saveOrUpdateAll(listPE);
		getHibernateTemplate().flush();
		System.out.println("Successfully loaded temp db with PEs :" +listPE.size());
		return listPE;
	}
	
	private List<Episode> loadEpisodes(List<EpisodeView> episodesView) {
		List<Episode> listEP = new ArrayList<Episode>();
		Episode tempEP = null;
		for (EpisodeView ep : episodesView) {
			tempEP = new Episode();
			Utility.transferEpisodeAttribute(ep,tempEP);
			tempEP.setEntitystatus(null);
			if(Utility.isMaster(ep)){
				tempEP.setType("M");
			}else{
				tempEP.setType("E");
			}
			if(findEpisodeByID(tempEP.getOnairEntityId().toString())==null){
				listEP.add(tempEP);
			}
		}
		if(isOnlyCompleted(episodesView)  && !listEP.isEmpty()){
			listEP.get(0).setEntitystatus(IEntityStatus.ENTITY_STATUS_COMPLETED);
			listEP.get(0).setEpisodeReleased("Y");
		}
		getHibernateTemplate().saveOrUpdateAll(listEP);
		getHibernateTemplate().flush();
		System.out.println("Successfully loaded temp db with EPs :" +listEP.size());
		return listEP;
	}
	

	private List<Episode> loadSeries(List<SeriesView> toBeLoadedList) {
		List<Episode> listSE = new ArrayList<Episode>();
		Episode tempEP = null;
		for (SeriesView series : toBeLoadedList) {
			tempEP = new Episode();
			Utility.transferSeriesAttribute(series,tempEP);
			tempEP.setEntitystatus(null);
			tempEP.setType("S");
			tempEP.setSeriesid(series.getOnairSeriesId());
			if(findEpisodeByID(tempEP.getOnairEntityId().toString())==null){
				listSE.add(tempEP);
			}
		}
		if(isOnlyCompleted(toBeLoadedList) && !listSE.isEmpty()){
			listSE.get(0).setEntitystatus(IEntityStatus.ENTITY_STATUS_COMPLETED);
			listSE.get(0).setEpisodeReleased("Y");
		}
		getHibernateTemplate().saveOrUpdateAll(listSE);
		getHibernateTemplate().flush();
		return listSE;
	}

	private boolean isOnlyCompleted(List<? extends BaseEntity> series) {
		if(series!=null && series.size()==1){
			if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(series.get(0).getEntityStatus())){
				return true;
			}
		}
		return false;
	}


	public List<Publicationevent> getPublicationEvents() {
		String hql = "from Publicationevent where processedstate=null order by lastprocessed asc";
		/*
		 * Retrieve all PEs where episodes are complete if there is none then we will retrieve all PEs where processedstate=null
		 * We mark episode complete field as Y in publicationevent table once the associated episode is completed. 
		 */
		List<Publicationevent> list=getPublicationEventsForEpisodeComplete();
		if(list==null || list.isEmpty()){
			list = (List<Publicationevent>) getHibernateTemplate().find(hql);
		}
		return list;
	}
	/**
	 * Local table=publicationevent
	 * In local table we mark pe as pvreleased ='Y' when we mark it as TO_PROCESS in on-air.
	 * We insert data in 'processedstate' field in local table when processing completes in on-air. Processed states are 'COMPLETE' ,'FAILED', 'PARKED'.
	 * Initially processed state will be null until processing completes in on-air.
	 * This means this method will pickup data from local table for PEs where TO_PROCESS is marked in on-air in previous schedule.
	 */
	public List<Publicationevent> getReleasedPublicationEvents() {
		String hql = "from Publicationevent where pvreleased ='Y' and processedstate is null order by lastprocessed asc";
		List<Publicationevent> list = (List<Publicationevent>) getHibernateTemplate().find(hql);
		return list;
	}
	/**
	 * Local table=episode
	 * In local table we mark ep as episodeReleased ='Y' when we mark it as TO_PROCESS in on-air.
	 * This method will pickup data from local table for EPs where TO_PROCESS is marked in on-air in previous schedule.
	 */
	public List<Episode> getReleasedEpisodes() {
		String hql = "from Episode where entitystatus ='TO_PROCESS' and episodeReleased='Y' order by lastprocessed asc";
		List<Episode> list = (List<Episode>) getHibernateTemplate().find(hql);
		return list;
	}
	
	public List<Publicationevent> getPublicationEventsForEpisodeComplete() {
		String hql = "from Publicationevent where processedstate=null and episodecompleted ='Y' order by lastprocessed asc";
		List<Publicationevent> episodeCompletedPubEvents = (List<Publicationevent>) getHibernateTemplate().find(hql);
		return episodeCompletedPubEvents;
	}
	public void updatePEAsEpisodeComplete(String uid) {
		String updatePubEvent = "update Publicationevent set episodecompleted='Y' where uid='"+uid+"'";
		getHibernateTemplate().bulkUpdate(updatePubEvent);
	}

	public List<Episode> findEpisodeByUid(String uid) {
		String hql = "from Episode where uid ='"+uid+"' order by lastprocessed asc";
		List<Episode> list = (List<Episode>) getHibernateTemplate().find(hql);
		return list;
		
	}
	
	public List<Episode> findEpisodeBySeriesID(String seriesID) {
		String hql = "from Episode where seriesid ="+seriesID+" and type='S' order by lastprocessed asc";
		List<Episode> list = (List<Episode>) getHibernateTemplate().find(hql);
		return list;
		
	}
	
	public List<Episode> loadToBeProcessedEpisodes(List<EpisodeView> fullList) {
		
		
		List<EpisodeView> toBeLoadedList = new ArrayList<EpisodeView>();
		if(fullList==null || fullList.isEmpty()){
			return null;
		}
		EpisodeView e= fullList.get(0);
		EpisodeView completedEpisode=null;
		String uid= e.getCoreNumber()+"/"+e.getVersionNumber();
		boolean isFailedParkedReached=false;
		BigDecimal onairEpisodeIDForParkedEP=null;
		List<Episode> loadedList=null;
		if(fullList!=null && !fullList.isEmpty()){
			for(EpisodeView ep:fullList){
				if((IEntityStatus.ENTITY_STATUS_PARKED.equalsIgnoreCase(ep.getEntityStatus()) && Utility.isInErrorList(ep.getErrorMsg()))||
						(IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(ep.getEntityStatus()) && Utility.isInErrorList(ep.getErrorMsg()))){
					isFailedParkedReached=true;
					onairEpisodeIDForParkedEP = ep.getOnairEpisodeId();
				}
				if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(ep.getEntityStatus())){
					completedEpisode=ep;
				}
				if(isFailedParkedReached && ep.getOnairEpisodeId().intValue()==onairEpisodeIDForParkedEP.intValue()){
					toBeLoadedList.add(ep);
				}
			}
		}
		if(toBeLoadedList.isEmpty() && completedEpisode!=null){
			toBeLoadedList.add(completedEpisode);
		}
		if(!toBeLoadedList.isEmpty()){
			loadedList =loadEpisodes(toBeLoadedList);
		}
		return loadedList;
	}

	public List<Episode> loadToBeProcessedSeries(List<SeriesView> fullList) {
		
		List<SeriesView> toBeLoadedList = new ArrayList<SeriesView>();
		if(fullList ==null || fullList.isEmpty()){
			return null;
		}
		SeriesView e= fullList.get(0);
		boolean isFailedParkedReached=false;
		SeriesView completedSeries=null;
		BigDecimal onairEpisodeIDForParkedEP=null;
		List<Episode> loadedSeries=null;
		if(fullList!=null && !fullList.isEmpty()){
			for(SeriesView series:fullList){
				if((IEntityStatus.ENTITY_STATUS_PARKED.equalsIgnoreCase(series.getEntityStatus())&& Utility.isInErrorList(series.getErrorMsg()))||
						(IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(series.getEntityStatus()) && Utility.isInErrorList(series.getErrorMsg()))){
					isFailedParkedReached=true;
				}
				if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(series.getEntityStatus())){
					completedSeries=series;
				}
				if(isFailedParkedReached){
					toBeLoadedList.add(series);
				}
			}
		}
		if(toBeLoadedList.isEmpty() && completedSeries!=null){
			toBeLoadedList.add(completedSeries);
		}
		if(!toBeLoadedList.isEmpty()){
			loadedSeries =loadSeries(toBeLoadedList);
		}
		return loadedSeries;
	}
	
	public List<Publicationevent> getAllPublicationEvents(Date fromDate, Date toDate) {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String startDate = df.format(fromDate);
		  String endDate = df.format(toDate);
		  String hql = "from Publicationevent where entitystatus!='FAILED' and lastprocessed>'"+startDate+"' and lastprocessed<='"+endDate+"'";
		  List<Publicationevent> list = (List<Publicationevent>) getHibernateTemplate().find(hql);
		  return list;
		 }
	public List<Episode> getAllEpisodes(Date fromDate, Date toDate) {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String startDate = df.format(fromDate);
		  String endDate = df.format(toDate);
		  String episodesHQL = "from Episode where entitystatus!='FAILED' and lastprocessed>'"+startDate+"' and lastprocessed<='"+endDate+"'";
		  List<Episode> list = (List<Episode>) getHibernateTemplate().find(episodesHQL);
		  return list;
	}
	
	public List<Episode> getStandAloneEpisodes() {
		  String episodesHQL = "from Episode where entitystatus is NULL order by lastprocessed asc";
		  List<Episode> list = (List<Episode>) getHibernateTemplate().find(episodesHQL);
		  return list;
	}
}
