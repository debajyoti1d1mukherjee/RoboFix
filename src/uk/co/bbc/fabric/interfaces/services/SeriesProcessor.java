package uk.co.bbc.fabric.interfaces.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import uk.co.bbc.fabric.interfaces.common.IEntityStatus;
import uk.co.bbc.fabric.interfaces.common.Utility;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDao;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDao;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

@Service
public class SeriesProcessor extends BaseEntityProcessor<SeriesView,BigDecimal,String> {

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
	public void process(SeriesView entity) throws OnairUpdateException{
		Episode series=derbyEntityDao.findEpisodeByID(entity.getOnairEntityId().toString());
		if(series==null){
			Episode series1= new Episode();
			Utility.transferSeriesAttribute(entity, series1);
			series1.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			series1.setEpisodeReleased("Y");
			series1.setReleaseTime(Calendar.getInstance().getTime());
			derbyEntityDao.insertEpisode(series1);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),series1);
			}catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}else{
			series.setEntitystatus(IEntityStatus.ENTITY_STATUS_TO_PROCESS);
			series.setEpisodeReleased("Y");
			series.setReleaseTime(Calendar.getInstance().getTime());
			derbyEntityDao.updateEntity(series);
			try{
				airEntityDao.updateOnairEntity(entity.getOnairEntityId().toString(),series);
			}catch(OnairUpdateException e){
				derbyEntityDao.updateEntity(e.getEntity());
				throw e;
			}
		}
	}

	public String handle(BigDecimal seriesId) throws OnairUpdateException{

		SeriesView seriesView = new SeriesView();
		seriesView.setOnairSeriesId(new BigDecimal(seriesId.toString()));
		derbyEntityDao.loadToBeProcessedSeries(airEntityDao.findBySeriesID(seriesView));
		List<Episode> serieses = derbyEntityDao.findEpisodeBySeriesID(seriesId.toString());
		if(serieses==null || serieses.isEmpty()){
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}else if(!Utility.isEntityValid(serieses)){
			derbyEntityDao.updateEntity(serieses.get(0));
			return IEntityStatus.ENTITY_STATUS_FAILED;
		}
		else if(Utility.isAllCompleted(serieses)){
			return IEntityStatus.ENTITY_STATUS_COMPLETED;
		}

		for(Episode series :serieses){
			//releasing all the relevant MEs one by one only if the ME present in onair database
			seriesView = airEntityDao.findSeriesByOnairEntityID(series.getOnairEntityId().toString());
			if(IEntityStatus.ENTITY_STATUS_COMPLETED.equalsIgnoreCase(seriesView.getEntityStatus())){
				continue;
			}
			else if(IEntityStatus.ENTITY_STATUS_TO_PROCESS.equalsIgnoreCase(seriesView.getEntityStatus())){
				System.out.println("Waiting for SE : " +seriesView.getOnairEntityId()+" to be completed.");
				return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
			}
			else if(isMasterExistsForSeries(seriesView.getOnairSeriesId())) {
				System.out.println("Series is getting processed "+seriesView.getOnairEntityId());
				process(seriesView);
				return IEntityStatus.ENTITY_STATUS_TO_PROCESS;
			} 
		}
		return null;
	}
	private boolean isMasterExistsForSeries(BigDecimal seriesId) {
		return airEntityDao.isMasterExistsForSeries(seriesId);
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
