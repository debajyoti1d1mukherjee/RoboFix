package uk.co.bbc.fabric.interfaces.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the publication_event_view database table.
 * 
 */
@Entity
@Table(name="publication_event_view")
//@NamedQuery(name="PublicationEventView.findAll", query="SELECT p FROM PublicationEventView p")
public class PublicationEventView extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action;

	@Column(name="actual_event_planned_id")
	private String actualEventPlannedId;

	@Column(name="audio_description_indicator")
	private String audioDescriptionIndicator;

	@Temporal(TemporalType.DATE)
	@Column(name="billed_date")
	private Date billedDate;

	@Column(name="billed_start_time")
	private String billedStartTime;

	@Column(name="billed_title")
	private String billedTitle;

	@Column(name="container_event_id")
	private String containerEventId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name="day_type")
	private String dayType;

	@Column(name="entity_status")
	private String entityStatus;

	@Column(name="episode_id")
	private String episodeId;

	@Column(name="error_msg")
	private String errorMsg;

	@Lob
	@Column(name="error_stacktrace")
	private String errorStacktrace;

	@Column(name="event_aspect_ratio_code")
	private String eventAspectRatioCode;

	@Column(name="event_aspect_ratio_description")
	private String eventAspectRatioDescription;

	@Column(name="event_id")
	private String eventId;

	@Column(name="event_live_code")
	private String eventLiveCode;

	@Column(name="event_live_description")
	private String eventLiveDescription;

	@Column(name="event_video_source_code")
	private String eventVideoSourceCode;

	@Column(name="event_video_source_description")
	private String eventVideoSourceDescription;

	@Column(name="fabric_update_status")
	private String fabricUpdateStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_processed")
	private Date lastProcessed;

	@Column(name="live_indicator")
	private String liveIndicator;

	@Id
	@Column(name="onair_entity_id")
	private BigInteger onairEntityId;

	@Column(name="optout_event_indicator")
	private byte optoutEventIndicator;

	@Column(name="parent_event_id")
	private String parentEventId;

	@Column(name="payload_sequence")
	private BigInteger payloadSequence;

	@Column(name="planned_event_indicator")
	private String plannedEventIndicator;

	@Column(name="processed_count")
	private int processedCount;

	@Temporal(TemporalType.DATE)
	@Column(name="publication_date")
	private Date publicationDate;

	@Column(name="publication_event_id")
	private BigInteger publicationEventId;

	@Column(name="publication_start_time")
	private String publicationStartTime;

	private String service;

	@Column(name="signed_indicator")
	private String signedIndicator;

	@Column(name="spool_number")
	private String spoolNumber;

	@Column(name="subtitle_indicator")
	private String subtitleIndicator;

	private String uid;

	public PublicationEventView() {
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActualEventPlannedId() {
		return this.actualEventPlannedId;
	}

	public void setActualEventPlannedId(String actualEventPlannedId) {
		this.actualEventPlannedId = actualEventPlannedId;
	}

	public String getAudioDescriptionIndicator() {
		return this.audioDescriptionIndicator;
	}

	public void setAudioDescriptionIndicator(String audioDescriptionIndicator) {
		this.audioDescriptionIndicator = audioDescriptionIndicator;
	}

	public Date getBilledDate() {
		return this.billedDate;
	}

	public void setBilledDate(Date billedDate) {
		this.billedDate = billedDate;
	}

	public String getBilledStartTime() {
		return this.billedStartTime;
	}

	public void setBilledStartTime(String billedStartTime) {
		this.billedStartTime = billedStartTime;
	}

	public String getBilledTitle() {
		return this.billedTitle;
	}

	public void setBilledTitle(String billedTitle) {
		this.billedTitle = billedTitle;
	}

	public String getContainerEventId() {
		return this.containerEventId;
	}

	public void setContainerEventId(String containerEventId) {
		this.containerEventId = containerEventId;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDayType() {
		return this.dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public String getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public String getEpisodeId() {
		return this.episodeId;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorStacktrace() {
		return this.errorStacktrace;
	}

	public void setErrorStacktrace(String errorStacktrace) {
		this.errorStacktrace = errorStacktrace;
	}

	public String getEventAspectRatioCode() {
		return this.eventAspectRatioCode;
	}

	public void setEventAspectRatioCode(String eventAspectRatioCode) {
		this.eventAspectRatioCode = eventAspectRatioCode;
	}

	public String getEventAspectRatioDescription() {
		return this.eventAspectRatioDescription;
	}

	public void setEventAspectRatioDescription(String eventAspectRatioDescription) {
		this.eventAspectRatioDescription = eventAspectRatioDescription;
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventLiveCode() {
		return this.eventLiveCode;
	}

	public void setEventLiveCode(String eventLiveCode) {
		this.eventLiveCode = eventLiveCode;
	}

	public String getEventLiveDescription() {
		return this.eventLiveDescription;
	}

	public void setEventLiveDescription(String eventLiveDescription) {
		this.eventLiveDescription = eventLiveDescription;
	}

	public String getEventVideoSourceCode() {
		return this.eventVideoSourceCode;
	}

	public void setEventVideoSourceCode(String eventVideoSourceCode) {
		this.eventVideoSourceCode = eventVideoSourceCode;
	}

	public String getEventVideoSourceDescription() {
		return this.eventVideoSourceDescription;
	}

	public void setEventVideoSourceDescription(String eventVideoSourceDescription) {
		this.eventVideoSourceDescription = eventVideoSourceDescription;
	}

	public String getFabricUpdateStatus() {
		return this.fabricUpdateStatus;
	}

	public void setFabricUpdateStatus(String fabricUpdateStatus) {
		this.fabricUpdateStatus = fabricUpdateStatus;
	}

	public Date getLastProcessed() {
		return this.lastProcessed;
	}

	public void setLastProcessed(Date lastProcessed) {
		this.lastProcessed = lastProcessed;
	}

	public String getLiveIndicator() {
		return this.liveIndicator;
	}

	public void setLiveIndicator(String liveIndicator) {
		this.liveIndicator = liveIndicator;
	}

	public BigInteger getOnairEntityId() {
		return this.onairEntityId;
	}

	public void setOnairEntityId(BigInteger onairEntityId) {
		this.onairEntityId = onairEntityId;
	}

	public byte getOptoutEventIndicator() {
		return this.optoutEventIndicator;
	}

	public void setOptoutEventIndicator(byte optoutEventIndicator) {
		this.optoutEventIndicator = optoutEventIndicator;
	}

	public String getParentEventId() {
		return this.parentEventId;
	}

	public void setParentEventId(String parentEventId) {
		this.parentEventId = parentEventId;
	}

	public BigInteger getPayloadSequence() {
		return this.payloadSequence;
	}

	public void setPayloadSequence(BigInteger payloadSequence) {
		this.payloadSequence = payloadSequence;
	}

	public String getPlannedEventIndicator() {
		return this.plannedEventIndicator;
	}

	public void setPlannedEventIndicator(String plannedEventIndicator) {
		this.plannedEventIndicator = plannedEventIndicator;
	}

	public int getProcessedCount() {
		return this.processedCount;
	}

	public void setProcessedCount(int processedCount) {
		this.processedCount = processedCount;
	}

	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public BigInteger getPublicationEventId() {
		return this.publicationEventId;
	}

	public void setPublicationEventId(BigInteger publicationEventId) {
		this.publicationEventId = publicationEventId;
	}

	public String getPublicationStartTime() {
		return this.publicationStartTime;
	}

	public void setPublicationStartTime(String publicationStartTime) {
		this.publicationStartTime = publicationStartTime;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSignedIndicator() {
		return this.signedIndicator;
	}

	public void setSignedIndicator(String signedIndicator) {
		this.signedIndicator = signedIndicator;
	}

	public String getSpoolNumber() {
		return this.spoolNumber;
	}

	public void setSpoolNumber(String spoolNumber) {
		this.spoolNumber = spoolNumber;
	}

	public String getSubtitleIndicator() {
		return this.subtitleIndicator;
	}

	public void setSubtitleIndicator(String subtitleIndicator) {
		this.subtitleIndicator = subtitleIndicator;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}