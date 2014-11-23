package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.util.Date;
import java.util.List;

import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.entity.EpisodeView;
import uk.co.bbc.fabric.interfaces.entity.PublicationEventView;
import uk.co.bbc.fabric.interfaces.entity.SeriesView;

public interface OnAirDerbyDao {
	public List<Publicationevent> getPublicationEvents();
	public List<Publicationevent> getReleasedPublicationEvents();
	public List<Episode> getReleasedEpisodes();
	public List<Publicationevent> getPublicationEventsForEpisodeComplete();
	public void updatePEAsEpisodeComplete(String uid);
	public List<Episode> findEpisodeByUid(String uid);
	public List<Episode> findEpisodeBySeriesID(String seriesID);
	public List<Episode> loadToBeProcessedEpisodes(List<EpisodeView> fullList);
	public List<Episode> loadToBeProcessedSeries(List<SeriesView> fullList);
	public List<Publicationevent> getAllPublicationEvents(Date fromDate, Date toDate);
	public List<Episode> getAllEpisodes(Date fromDate, Date toDate);
	public List<Episode> getStandAloneEpisodes();
	public List<Publicationevent> loadToBeProcessedPublicationEvents(List<PublicationEventView> publicationEventsForProcessing);
	public void updateEpisodesAsFailed(String uid);
	public void updateEntity(BaseEntity dEpisode);
	public Publicationevent findPEByID(String onairEntityID);
	public Episode findEpisodeByID(String onairEntityID);
	public void insertEpisode(BaseEntity dEpisode);
}
