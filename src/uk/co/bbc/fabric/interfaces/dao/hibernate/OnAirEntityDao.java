package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.OnAirEntity;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;
public interface OnAirEntityDao {

		
	public List<PublicationEventView> getPublicationEventsForCurrentDate();
	
	public void updateOnairEntity(String onairEntityId,BaseEntity entity) throws OnairUpdateException;
	public boolean isMasterExistsForSeries(BigDecimal seriesId);
	public List<PublicationEventView> getPublicationEventsForProcessing();

	public List<EpisodeView> findEpisodeByCoreNumberVersionNumber(EpisodeView episode);
	public EpisodeView findByEpisodeID(EpisodeView entity);
	public List<SeriesView> findBySeriesID(SeriesView entity);
	public OnAirEntity findOnAirEntityByID(String entityID);
	
	public EpisodeView findByEpisodeOnAirEntityID(String onairEntityId);

	PublicationEventView findPublicationEventByEntityID(String onairEntityID);

	public List<EpisodeView> getStandaloneEpisodeUIDs();

	public SeriesView findBySeriesID(String string);
	
	public SeriesView findSeriesByOnairEntityID(String string);
	
	public List<Publicationevent> getAllFailedPublicationEvents(Date fromDate, Date toDate);
	
	public List<Episode> getAllFailedEpisodes(Date fromDate, Date toDate);
	
	public List<Episode> getAllFailedSeries(Date fromDate, Date toDate);
	
}
