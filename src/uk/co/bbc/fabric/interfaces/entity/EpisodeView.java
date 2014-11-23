package uk.co.bbc.fabric.interfaces.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the episode_view database table.
 * 
 */
@Entity
@Table(name="episode_view")
//@NamedQuery(name="EpisodeView.findAll", query="SELECT e FROM EpisodeView e")
public class EpisodeView extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action;

	@Column(name="audio_description_type")
	private String audioDescriptionType;

	@Temporal(TemporalType.DATE)
	@Column(name="available_to_schedule_date")
	private Date availableToScheduleDate;

	@Column(name="available_to_schedule_date_precision")
	private String availableToScheduleDatePrecision;

	@Lob
	@Column(name="billing_synopsis")
	private String billingSynopsis;

	private String bupid;

	@Column(name="canonical_version_ref_episode_id")
	private BigDecimal canonicalVersionRefEpisodeId;

	private byte composition;

	private String confidentiality;

	@Column(name="core_number")
	private String coreNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Lob
	@Column(name="creation_notes")
	private String creationNotes;

	@Temporal(TemporalType.DATE)
	@Column(name="delivery_date")
	private Date deliveryDate;

	@Lob
	@Column(name="editorial_assement")
	private String editorialAssement;

	@Column(name="entity_status")
	private String entityStatus;

	@Column(name="episode_id")
	private BigInteger episodeId;

	@Column(name="episode_number")
	private BigDecimal episodeNumber;

	@Column(name="error_msg")
	private String errorMsg;

	@Lob
	@Column(name="error_stacktrace")
	private String errorStacktrace;

	@Temporal(TemporalType.DATE)
	@Column(name="first_version_tx_date")
	private Date firstVersionTxDate;

	@Column(name="gross_duration")
	private BigDecimal grossDuration;

	@Column(name="high_definition")
	private byte highDefinition;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_processed")
	private Date lastProcessed;

	private byte live;

	@Column(name="ofcom_editorial_category_level1")
	private String ofcomEditorialCategoryLevel1;

	@Column(name="ofcom_editorial_category_level2")
	private String ofcomEditorialCategoryLevel2;

	@Column(name="ofcom_editorial_category_level3")
	private String ofcomEditorialCategoryLevel3;

	@Column(name="ofcom_genre_primary")
	private String ofcomGenrePrimary;

	@Column(name="ofcom_genre_secondary")
	private String ofcomGenreSecondary;

	@Id
	@Column(name="onair_entity_id")
	private BigInteger onairEntityId;

	@Column(name="onair_episode_id")
	private BigDecimal onairEpisodeId;

	@Temporal(TemporalType.DATE)
	@Column(name="original_tx_date")
	private Date originalTxDate;

	@Column(name="payload_sequence")
	private BigInteger payloadSequence;

	@Column(name="post_watershed")
	private Boolean postWatershed;
	
	@Column(name="processed_count")
	private int processedCount;

	@Column(name="published_title")
	private String publishedTitle;

	@Column(name="scheduling_contract_id")
	private BigDecimal schedulingContractId;

	@Column(name="series_id")
	private BigDecimal seriesId;

	@Column(name="signing_type")
	private String signingType;

	@Column(name="subtitle_type")
	private String subtitleType;

	@Lob
	private String summary;

	@Column(name="supplier_type")
	private String supplierType;

	@Lob
	private String synopsis;

	private String title;

	private String topicality;

	private String type;

	@Column(name="version_number")
	private String versionNumber;

	@Column(name="version_reason")
	private String versionReason;

	public EpisodeView() {
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAudioDescriptionType() {
		return this.audioDescriptionType;
	}

	public void setAudioDescriptionType(String audioDescriptionType) {
		this.audioDescriptionType = audioDescriptionType;
	}

	public Date getAvailableToScheduleDate() {
		return this.availableToScheduleDate;
	}

	public void setAvailableToScheduleDate(Date availableToScheduleDate) {
		this.availableToScheduleDate = availableToScheduleDate;
	}

	public String getAvailableToScheduleDatePrecision() {
		return this.availableToScheduleDatePrecision;
	}

	public void setAvailableToScheduleDatePrecision(String availableToScheduleDatePrecision) {
		this.availableToScheduleDatePrecision = availableToScheduleDatePrecision;
	}

	public String getBillingSynopsis() {
		return this.billingSynopsis;
	}

	public void setBillingSynopsis(String billingSynopsis) {
		this.billingSynopsis = billingSynopsis;
	}

	public String getBupid() {
		return this.bupid;
	}

	public void setBupid(String bupid) {
		this.bupid = bupid;
	}

	public BigDecimal getCanonicalVersionRefEpisodeId() {
		return this.canonicalVersionRefEpisodeId;
	}

	public void setCanonicalVersionRefEpisodeId(BigDecimal canonicalVersionRefEpisodeId) {
		this.canonicalVersionRefEpisodeId = canonicalVersionRefEpisodeId;
	}

	public byte getComposition() {
		return this.composition;
	}

	public void setComposition(byte composition) {
		this.composition = composition;
	}

	public String getConfidentiality() {
		return this.confidentiality;
	}

	public void setConfidentiality(String confidentiality) {
		this.confidentiality = confidentiality;
	}

	public String getCoreNumber() {
		return this.coreNumber;
	}

	public void setCoreNumber(String coreNumber) {
		this.coreNumber = coreNumber;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreationNotes() {
		return this.creationNotes;
	}

	public void setCreationNotes(String creationNotes) {
		this.creationNotes = creationNotes;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getEditorialAssement() {
		return this.editorialAssement;
	}

	public void setEditorialAssement(String editorialAssement) {
		this.editorialAssement = editorialAssement;
	}

	public String getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public BigInteger getEpisodeId() {
		return this.episodeId;
	}

	public void setEpisodeId(BigInteger episodeId) {
		this.episodeId = episodeId;
	}

	public BigDecimal getEpisodeNumber() {
		return this.episodeNumber;
	}

	public void setEpisodeNumber(BigDecimal episodeNumber) {
		this.episodeNumber = episodeNumber;
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

	public Date getFirstVersionTxDate() {
		return this.firstVersionTxDate;
	}

	public void setFirstVersionTxDate(Date firstVersionTxDate) {
		this.firstVersionTxDate = firstVersionTxDate;
	}

	public BigDecimal getGrossDuration() {
		return this.grossDuration;
	}

	public void setGrossDuration(BigDecimal grossDuration) {
		this.grossDuration = grossDuration;
	}

	public byte getHighDefinition() {
		return this.highDefinition;
	}

	public void setHighDefinition(byte highDefinition) {
		this.highDefinition = highDefinition;
	}

	public Date getLastProcessed() {
		return this.lastProcessed;
	}

	public void setLastProcessed(Date lastProcessed) {
		this.lastProcessed = lastProcessed;
	}

	public byte getLive() {
		return this.live;
	}

	public void setLive(byte live) {
		this.live = live;
	}

	public String getOfcomEditorialCategoryLevel1() {
		return this.ofcomEditorialCategoryLevel1;
	}

	public void setOfcomEditorialCategoryLevel1(String ofcomEditorialCategoryLevel1) {
		this.ofcomEditorialCategoryLevel1 = ofcomEditorialCategoryLevel1;
	}

	public String getOfcomEditorialCategoryLevel2() {
		return this.ofcomEditorialCategoryLevel2;
	}

	public void setOfcomEditorialCategoryLevel2(String ofcomEditorialCategoryLevel2) {
		this.ofcomEditorialCategoryLevel2 = ofcomEditorialCategoryLevel2;
	}

	public String getOfcomEditorialCategoryLevel3() {
		return this.ofcomEditorialCategoryLevel3;
	}

	public void setOfcomEditorialCategoryLevel3(String ofcomEditorialCategoryLevel3) {
		this.ofcomEditorialCategoryLevel3 = ofcomEditorialCategoryLevel3;
	}

	public String getOfcomGenrePrimary() {
		return this.ofcomGenrePrimary;
	}

	public void setOfcomGenrePrimary(String ofcomGenrePrimary) {
		this.ofcomGenrePrimary = ofcomGenrePrimary;
	}

	public String getOfcomGenreSecondary() {
		return this.ofcomGenreSecondary;
	}

	public void setOfcomGenreSecondary(String ofcomGenreSecondary) {
		this.ofcomGenreSecondary = ofcomGenreSecondary;
	}

	public BigInteger getOnairEntityId() {
		return this.onairEntityId;
	}

	public void setOnairEntityId(BigInteger onairEntityId) {
		this.onairEntityId = onairEntityId;
	}

	public BigDecimal getOnairEpisodeId() {
		return this.onairEpisodeId;
	}

	public void setOnairEpisodeId(BigDecimal onairEpisodeId) {
		this.onairEpisodeId = onairEpisodeId;
	}

	public Date getOriginalTxDate() {
		return this.originalTxDate;
	}

	public void setOriginalTxDate(Date originalTxDate) {
		this.originalTxDate = originalTxDate;
	}

	public BigInteger getPayloadSequence() {
		return this.payloadSequence;
	}

	public void setPayloadSequence(BigInteger payloadSequence) {
		this.payloadSequence = payloadSequence;
	}

	public Boolean getPostWatershed() {
		return this.postWatershed;
	}

	public void setPostWatershed(Boolean postWatershed) {
		this.postWatershed = postWatershed;
	}

	public int getProcessedCount() {
		return this.processedCount;
	}

	public void setProcessedCount(int processedCount) {
		this.processedCount = processedCount;
	}

	public String getPublishedTitle() {
		return this.publishedTitle;
	}

	public void setPublishedTitle(String publishedTitle) {
		this.publishedTitle = publishedTitle;
	}

	public BigDecimal getSchedulingContractId() {
		return this.schedulingContractId;
	}

	public void setSchedulingContractId(BigDecimal schedulingContractId) {
		this.schedulingContractId = schedulingContractId;
	}

	public BigDecimal getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(BigDecimal seriesId) {
		this.seriesId = seriesId;
	}

	public String getSigningType() {
		return this.signingType;
	}

	public void setSigningType(String signingType) {
		this.signingType = signingType;
	}

	public String getSubtitleType() {
		return this.subtitleType;
	}

	public void setSubtitleType(String subtitleType) {
		this.subtitleType = subtitleType;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSupplierType() {
		return this.supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	public String getSynopsis() {
		return this.synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTopicality() {
		return this.topicality;
	}

	public void setTopicality(String topicality) {
		this.topicality = topicality;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersionNumber() {
		return this.versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionReason() {
		return this.versionReason;
	}

	public void setVersionReason(String versionReason) {
		this.versionReason = versionReason;
	}

}