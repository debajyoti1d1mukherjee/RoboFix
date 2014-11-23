package uk.co.bbc.fabric.interfaces.dao.hibernate;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Repository
public class OnAirEntityDaoImpl extends HibernateEntitylessDao implements OnAirEntityDao {

	@Resource(name="myProperties")
	
	private Properties myProperties;
	
	private List<String> alreadyPickedUpEpisodes =null;
	private List<EpisodeView> standaloneEpisodes =null;
	private static String currentDate =Utility.getCurrentDate();
	
	public static String errorMsg =null;
	 @PostConstruct
	  public void init() {
	    System.out.println("myProperties in DAO"+myProperties);
	    if(myProperties!=null){
	    	Enumeration errorKeys = myProperties.keys();
	    	while(errorKeys.hasMoreElements()){
	    		//System.out.println("Keys"+errorKeys.nextElement().toString());
	    		if(errorMsg==null){
	    			errorMsg = "'"+myProperties.getProperty(errorKeys.nextElement().toString())+"'";
	    		}else{
	    			errorMsg = errorMsg+",'"+myProperties.getProperty(errorKeys.nextElement().toString())+"'";
	    		}
	    	}
	    	System.out.println("Error Message"+errorMsg);
	    }
	  }
		 
	 @Override
	 public PublicationEventView findPublicationEventByEntityID(String onairEntityID) {
		 String hql = "from PublicationEventView e where e.onairEntityId="+onairEntityID+" order by e.created asc";
		 List<PublicationEventView> list = (List<PublicationEventView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public EpisodeView findByEpisodeID(EpisodeView entity) {
		 BigDecimal episodeID =entity.getOnairEpisodeId();                   
		 String hql = "from EpisodeView e where e.onairEpisodeId="+episodeID+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<EpisodeView> list = (List<EpisodeView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public EpisodeView findByEpisodeOnAirEntityID(String onairEntityId) {
		 String hql = "from EpisodeView e where e.onairEntityId="+onairEntityId+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<EpisodeView> list = (List<EpisodeView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public OnAirEntity findOnAirEntityByID(String entityID) {
		 String hql = "from OnAirEntity e where e.onairEntityId="+entityID+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<OnAirEntity> list = (List<OnAirEntity>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public List<SeriesView> findBySeriesID(SeriesView entity) {
		 BigDecimal seriesID =entity.getOnairSeriesId();                   
		 String hql = "from SeriesView e where e.onairSeriesId="+seriesID+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<SeriesView> list = (List<SeriesView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list;
	 }
	 
	 @Override
	 public SeriesView findBySeriesID(String seriesID){
		 String hql = "from SeriesView e where e.onairSeriesId="+seriesID+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<SeriesView> list = (List<SeriesView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public SeriesView findSeriesByOnairEntityID(String entityID){
		 String hql = "from SeriesView e where e.onairEntityId="+entityID+" order by e.created asc";
		 System.out.println(" HQL : "+hql);
		 List<SeriesView> list = (List<SeriesView>)getHibernateTemplate().find(hql);
		 if(list==null || list.isEmpty()){
			 return null;
		 }
		 return list.get(0);
	 }
	 
	 @Override
	 public List<EpisodeView> getStandaloneEpisodeUIDs(){
		 
		 getPublicationEventsForEpisodes(getEpisodesForCurrentDate());
			
		 List<String> standaloneEpisodeUIDs= new ArrayList<String>();
		 if(standaloneEpisodes!=null && !standaloneEpisodes.isEmpty()){
			 System.out.println("Standanloe E s :");
			 for(EpisodeView e:standaloneEpisodes){
				 System.out.print(e.getCoreNumber()+"/"+e.getVersionNumber()+" ");
			 }
			 return standaloneEpisodes;
		 }
		 return null;
	 }
	 
	//Get all Failed/Parked PEs for a date with errors as in properties file .
	 // Union PEs with same event id and UUID and creted date> failed time stamp of PEs
	@Override
	public List<PublicationEventView> getPublicationEventsForCurrentDate(){
		//Provide implementation to get all PEs for a date with errors as in properties file.
		
		List<PublicationEventView> publicationEvents = new ArrayList<PublicationEventView>();
		publicationEvents = getHibernateTemplate().find("from PublicationEventView where entityStatus in ('PARKED') and errorMsg in ("+errorMsg+") and uid='WENX717P/01'");
		List<PublicationEventView> pubEventsForWebServiceException = getHibernateTemplate().find("from PublicationEventView where entityStatus in ('PARKED') and errorMsg like 'EM3 failure:%' and errorMsg like '%SRV%' and created>'"+currentDate+"'");
		if(pubEventsForWebServiceException!=null && !pubEventsForWebServiceException.isEmpty()){
			if(publicationEvents!=null){
				publicationEvents.addAll(pubEventsForWebServiceException);
			}
		}
		System.out.println("List publicationEvents:"+publicationEvents );
		List<String> pickedUpEventIds = new ArrayList<String>();
		if(publicationEvents!=null && !publicationEvents.isEmpty()){
			int currentSize = publicationEvents.size();
			for(int i=0;i<currentSize;i++){
				PublicationEventView pe =publicationEvents.get(i);
				String eventID = pe.getEventId();
				String uid = pe.getUid();
				if(pickedUpEventIds.contains(eventID)){
					System.out.println("Discarding Duplicates : "+eventID);
					continue;
				}else{
					pickedUpEventIds.add(eventID);
				}
				Date createdDate =pe.getCreated();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String created = df.format(createdDate);
				String hql ="from PublicationEventView where eventId='"+eventID+"' and uid='"+uid+"' and created>'"+created+"'";
				List<PublicationEventView> linkedPEs =(List<PublicationEventView>)getHibernateTemplate().find(hql);
				if(linkedPEs!=null && !linkedPEs.isEmpty()){
					for(PublicationEventView lpe:linkedPEs){
						if(lpe.getOnairEntityId().intValue()!=pe.getOnairEntityId().intValue()){
							publicationEvents.add(lpe);
						}
					}
				}
				
			}
		}
		
		return publicationEvents;
	}
	/*
	 * Retrieve PEs for failed EPs to give priority for failed EPs.
	 * Once completed process stand-alone PEs
	 * 
	 */
	@Override
	public List<PublicationEventView> getPublicationEventsForProcessing(){
		//Provide implementation to get all PEs for a date with errors as in properties file.
		List<PublicationEventView> publicationEvents = new ArrayList<PublicationEventView>();
		publicationEvents.addAll(getPublicationEventsForEpisodes(getEpisodesForCurrentDate()));
		if(publicationEvents.isEmpty()){
			publicationEvents = getPublicationEventsForCurrentDate();
		}
		return publicationEvents;
	}
	
	//Get all Failed/Parked EPs for a date with errors as in properties file.	
	private List<EpisodeView> getEpisodesForCurrentDate(){
		
		List<EpisodeView> episodes = new ArrayList<EpisodeView>();
		//String hql ="from EpisodeView where entityStatus in ('PARKED') and errorMsg in ("+errorMsg+") and created>'2014-07-01 00:00:01' and created<'2014-07-31 23:59:59'";
		String hql ="from EpisodeView where entityStatus in ('PARKED') and errorMsg in ("+errorMsg+")and core_number='CBIG021R' and version_number='01'";
		System.out.println(" Episodes hql : "+hql);
		episodes = getHibernateTemplate().find(hql);
		hql ="from EpisodeView where entityStatus in ('PARKED') and errorMsg like 'EM3 failure:%' and errorMsg like '%SRV%' and created>'"+currentDate+"'";
		List<EpisodeView> episodesForEM3Failure = getHibernateTemplate().find(hql);
		if(episodesForEM3Failure!=null && !episodesForEM3Failure.isEmpty()){
			if(episodes!=null){
				//episodes.addAll(episodesForEM3Failure);
			}
		}
		return episodes;
	}
	/*
	 * This method will retrieve all the relevant PEs against failed/parked episodes which needs to be released.
	 * The PEs are picked up by the following logic
	 * -Timestamp of  PEs >= Timestamp of failed episode UIINON orphaned PEs
	 */
	private List<PublicationEventView> getPublicationEventsForEpisodes(List<EpisodeView> episodes){
		List<PublicationEventView> publicationEvents = new ArrayList<PublicationEventView>();
		alreadyPickedUpEpisodes = new ArrayList<String>();
		standaloneEpisodes = new ArrayList<EpisodeView>();
		for(EpisodeView episode:episodes){
			String c=episode.getCoreNumber();
			String v = episode.getVersionNumber();
			String uid=c+"/"+v;
			if(alreadyPickedUpEpisodes.contains(uid)){
				System.out.println("Discarding Duplicates :"+uid);
				continue;
			}
			else{
				if(Utility.isDryRun() && getHibernateTemplate().find("from PublicationEventView where uid='"+uid+"'").isEmpty()){
					standaloneEpisodes.add(episode);
					alreadyPickedUpEpisodes.add(uid);
				}
				if(!Utility.isDryRun()){
					standaloneEpisodes.add(episode);
					alreadyPickedUpEpisodes.add(uid);
				}
			}
			Date createdDate =episode.getLastProcessed();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String created = df.format(createdDate);
			EpisodeView firstEpisode =getFirstEpisode(episode);
			String firstCreated = df.format(firstEpisode.getLastProcessed());
			String hql1="from PublicationEventView where uid='"+uid+"' and (lastProcessed >'"+created+"' or lastProcessed<'"+firstCreated+"')";
			String hql2="from PublicationEventView where entityStatus!='COMPLETED' and uid='"+uid+"' and (lastProcessed <'"+created+"' and lastProcessed>'"+firstCreated+"')";
			publicationEvents.addAll(getHibernateTemplate().find(hql1));
			publicationEvents.addAll(getHibernateTemplate().find(hql2));
		}
		System.out.println(" Total episodes : "+episodes.size());
		System.out.println(" total pe for episodes : "+publicationEvents.size());
		return publicationEvents;
	}
	
	
	private EpisodeView getFirstEpisode(EpisodeView episode){
		String c=episode.getCoreNumber();
		String v = episode.getVersionNumber();
		List<EpisodeView> episodes = new ArrayList<EpisodeView>();
		String hql ="from EpisodeView e where e.coreNumber='"+c+"'"+"and e.versionNumber='"+v+"' order by e.created asc";
		episodes = getHibernateTemplate().find(hql);
		return episodes.get(0);
	}
	
	@Override
	public void updateOnairEntity(String onairEntityId,BaseEntity entity) throws OnairUpdateException{
		try{
			String hql ="update OnAirEntity set entityStatus='TO_PROCESS'"+" where onairEntityId='"+onairEntityId+"'";
			System.out.println("Update HQL : "+hql);
			if(!Utility.isDryRun()){
				getHibernateTemplate().bulkUpdate(hql);
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String trace = sw.getBuffer().toString();
			if(trace!=null && trace.length()>1000){
				trace = trace.substring(0, 1000);
			}
			if(entity instanceof Episode){
				Episode episode =(Episode)entity;
				episode.setEntitystatus(null);
				episode.setEpisodeReleased("N");
				episode.setReleaseTime(null);
				episode.setErrorMessage(trace);
			}else if(entity instanceof Publicationevent){
				Publicationevent pe =(Publicationevent)entity;
				pe.setPvreleased("N");
				pe.setProcessedstate(null);
				pe.setReleaseTime(null);
				pe.setErrorMessage(trace);
			}
			
			OnairUpdateException ex = new OnairUpdateException(entity);
			throw ex;
		}
	}
	
	@Override
	public boolean isMasterExistsForSeries(BigDecimal seriesId){
		String hql = "from EpisodeView e where e.seriesId = '"+seriesId+"' order by e.lastProcessed asc";
		List<EpisodeView> masterEpisodeForSeries = (List<EpisodeView>)getHibernateTemplate().find(hql);
		if(!masterEpisodeForSeries.isEmpty()){
			EpisodeView masterEpisode =masterEpisodeForSeries.get(0);
			BigDecimal episodeID = masterEpisode.getOnairEpisodeId();
			System.out.println("Episode id : "+episodeID);
			System.out.println("Can ref id : "+masterEpisode.getCanonicalVersionRefEpisodeId());
			if("-1".equalsIgnoreCase(""+masterEpisode.getCanonicalVersionRefEpisodeId())){
				return true;
			}else if(episodeID.intValue()==masterEpisode.getCanonicalVersionRefEpisodeId().intValue()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<EpisodeView> findEpisodeByCoreNumberVersionNumber(EpisodeView episode) {
		String c=episode.getCoreNumber();
		String v = episode.getVersionNumber();
		String hql = "from EpisodeView e where e.coreNumber = '"+c+"'and e.versionNumber='"+v+"' order by e.created asc";
		List<EpisodeView> episodes = (List<EpisodeView>)getHibernateTemplate().find(hql);
		return episodes;
	}
	
	public List<Publicationevent> getAllFailedPublicationEvents(Date fromDate, Date toDate) {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  String startDate = df.format(fromDate);
		  String endDate = df.format(toDate);
		  String hql = "from PublicationEventView where entityStatus in ('FAILED','CANNOT_PROCESS') and lastProcessed>'"+startDate+"' and lastProcessed<='"+endDate+"'";
		  List<PublicationEventView> list = (List<PublicationEventView>) getHibernateTemplate().find(hql);
		  List<Publicationevent> retList = new ArrayList<Publicationevent>();
		  if(list!=null && !list.isEmpty()){
			  for(PublicationEventView publicationEvent:list){
				  Publicationevent pe = new Publicationevent();
				  Utility.transferPublicationEventAttribute(publicationEvent, pe);
				  retList.add(pe);
			  }
		  }
		  return retList;
	}
	public List<Episode> getAllFailedEpisodes(Date fromDate, Date toDate) {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String startDate = df.format(fromDate);
		  String endDate = df.format(toDate);
		  String episodesHQL = "from EpisodeView where entityStatus in ('FAILED','CANNOT_PROCESS') and lastProcessed>'"+startDate+"' and lastProcessed<='"+endDate+"'";
		  List<EpisodeView> list = (List<EpisodeView>) getHibernateTemplate().find(episodesHQL);
		  List<Episode> retList = new ArrayList<Episode>();
		  if(list!=null && !list.isEmpty()){
			  for(EpisodeView ep:list){
				  Episode e = new Episode();
				  Utility.transferEpisodeAttribute(ep, e);
				  if(Utility.isMaster(ep)){
					  e.setType("M");
				  }else{
					  e.setType("E");
				  }
				  retList.add(e);
			  }
		  }
		  return retList;
	}
	
	public List<Episode> getAllFailedSeries(Date fromDate, Date toDate) {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String startDate = df.format(fromDate);
		  String endDate = df.format(toDate);
		  String episodesHQL = "from SeriesView where entityStatus in ('FAILED','CANNOT_PROCESS') and lastProcessed>'"+startDate+"' and lastProcessed<='"+endDate+"'";
		  List<SeriesView> list = (List<SeriesView>) getHibernateTemplate().find(episodesHQL);
		  List<Episode> retList = new ArrayList<Episode>();
		  if(list!=null && !list.isEmpty()){
			  for(SeriesView series:list){
				  Episode se = new Episode();
				  Utility.transferSeriesAttribute(series, se);
				  retList.add(se);
			  }
		  }
		  return retList;
	}
}
