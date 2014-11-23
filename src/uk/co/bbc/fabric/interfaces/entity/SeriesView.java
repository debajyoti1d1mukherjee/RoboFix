package uk.co.bbc.fabric.interfaces.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the series_view database table.
 * 
 */
@Entity
@Table(name="series_view")
//@NamedQuery(name="SeriesView.findAll", query="SELECT s FROM SeriesView s")
public class SeriesView extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name="entity_status")
	private String entityStatus;

	@Column(name="error_msg")
	private String errorMsg;

	@Lob
	@Column(name="error_stacktrace")
	private String errorStacktrace;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_processed")
	private Date lastProcessed;

	@Id
	@Column(name="onair_entity_id")
	private BigInteger onairEntityId;

	@Column(name="onair_series_id")
	private BigDecimal onairSeriesId;

	@Column(name="payload_sequence")
	private BigInteger payloadSequence;

	@Column(name="processed_count")
	private int processedCount;

	@Column(name="series_id")
	private BigInteger seriesId;

	private String synopsis;

	private String title;

	private String type;

	@Column(name="working_title")
	private String workingTitle;

	public SeriesView() {
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
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

	public Date getLastProcessed() {
		return this.lastProcessed;
	}

	public void setLastProcessed(Date lastProcessed) {
		this.lastProcessed = lastProcessed;
	}

	public BigInteger getOnairEntityId() {
		return this.onairEntityId;
	}

	public void setOnairEntityId(BigInteger onairEntityId) {
		this.onairEntityId = onairEntityId;
	}

	public BigDecimal getOnairSeriesId() {
		return this.onairSeriesId;
	}

	public void setOnairSeriesId(BigDecimal onairSeriesId) {
		this.onairSeriesId = onairSeriesId;
	}

	public BigInteger getPayloadSequence() {
		return this.payloadSequence;
	}

	public void setPayloadSequence(BigInteger payloadSequence) {
		this.payloadSequence = payloadSequence;
	}

	public int getProcessedCount() {
		return this.processedCount;
	}

	public void setProcessedCount(int processedCount) {
		this.processedCount = processedCount;
	}

	public BigInteger getSeriesId() {
		return this.seriesId;
	}

	public void setSeriesId(BigInteger seriesId) {
		this.seriesId = seriesId;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWorkingTitle() {
		return this.workingTitle;
	}

	public void setWorkingTitle(String workingTitle) {
		this.workingTitle = workingTitle;
	}

}