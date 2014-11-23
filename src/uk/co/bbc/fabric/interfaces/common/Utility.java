package uk.co.bbc.fabric.interfaces.common;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDaoImpl;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDaoImpl;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;

public class Utility {
	
	public static String KnownError ="Read timed out,Warning: Commissioning Adapter: Series is missing for incoming episode, will retry later,Warning: Commissioning Adapter: Master episode is missing for incoming episode, will retry later";
	
	public static void transferEpisodeAttribute(EpisodeView fromEpisode, Episode toEpisode) {
		toEpisode.setCreated(fromEpisode.getCreated());
		toEpisode.setEntitystatus(fromEpisode.getEntityStatus());
		toEpisode.setLastprocessed(fromEpisode.getLastProcessed());
		toEpisode.setOnairentityid(fromEpisode.getOnairEntityId().longValue());
		toEpisode.setSeriesid(fromEpisode.getSeriesId());
		String uid = fromEpisode.getCoreNumber()+"/"+fromEpisode.getVersionNumber();
		toEpisode.setUid(uid);
	}

	public static void transferPublicationEventAttribute(
			PublicationEventView source, Publicationevent dest) {
		dest.setEntitystatus(source.getEntityStatus());
		dest.setCreated(source.getCreated());
		dest.setOnairentityid(source.getOnairEntityId().longValue());
		dest.setLastprocessed(source.getLastProcessed());
		dest.setUid(source.getUid());
	}

	public static void transferSeriesAttribute(SeriesView source,
			Episode dest) {
		dest.setEntitystatus(source.getEntityStatus());
		dest.setCreated(source.getCreated());
		dest.setOnairentityid(source.getOnairEntityId().longValue());
		dest.setLastprocessed(source.getLastProcessed());
		dest.setSeriesid(new BigDecimal(source.getSeriesId()));
		dest.setType("S");
	}
	public static boolean isAllCompleted(List<?> entities) {
		boolean retVal =true;
		if(entities!=null && !entities.isEmpty()){
			for(Object ep:entities){
				if(!IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(((BaseEntity)ep).getEntityStatus())){
					retVal=false;
					break;
				}
			}
		}
		return retVal;
	}
	
	public static boolean isEntityValid(List<?> episodes) {
		boolean retVal =true;
		if(episodes!=null && !episodes.isEmpty()){
			for(Object ep:episodes){
				if(IEntityStatus.ENTITY_STATUS_FAILED.equalsIgnoreCase(((BaseEntity)ep).getEntityStatus())){
					retVal=false;
					break;
				}
			}
		}
		return retVal;
	}
	
	public static boolean isMaster(EpisodeView episode) {
		
		if(episode !=null && "-1".equalsIgnoreCase(episode.getCanonicalVersionRefEpisodeId().toString())){
			return true;
		}
		if(episode !=null && episode.getOnairEpisodeId().intValue()==episode.getCanonicalVersionRefEpisodeId().intValue()){
			return true;
		}
		return false;
	}
	
	public static String getCurrentDate() {
		String today=null;
		Date currentDate = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String stringCurrentDate = df.format(currentDate);
		Date d = new Date(stringCurrentDate);
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		today = df.format(d);
		return today;
	}
	
	public static Date getCurrentTimeInGMT() {
		Date date = new Date();
	    DateFormat gmtFormat = new SimpleDateFormat();
	    TimeZone gmtTime = TimeZone.getTimeZone("Europe/London");
	    gmtFormat.setTimeZone(gmtTime);
	    return new Date(gmtFormat.format(date));
	}
	
	public static boolean isInErrorList(String errorMsg) {
		return OnAirEntityDaoImpl.errorMsg.contains(errorMsg) ||(errorMsg.contains("EM3 failure:") && errorMsg.contains("SRV"));
	}
	
	public static boolean isDryRun() {
		return  "true".equalsIgnoreCase(OnAirDerbyDaoImpl.isDryRun)?true:false;
	}
	
	public static boolean isFailed(OnAirEntity entity) {
		boolean retVal = true;
		
		if(entity.getErrorMsg() == null || KnownError.contains(entity.getErrorMsg())){
			retVal = false;
		}
		return  retVal;
	}
	
	
}
