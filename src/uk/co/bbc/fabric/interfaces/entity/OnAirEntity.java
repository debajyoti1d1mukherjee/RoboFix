package uk.co.bbc.fabric.interfaces.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the onair_entity database table.
 * 
 */
@Component
@Entity
@Table(name="onair_entity")
//@NamedQuery(name="OnairEntity.findAll", query="SELECT o FROM OnairEntity o")
public class OnAirEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="onair_entity_id")
	private String onairEntityId;

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

	@Column(name="payload_sequence")
	private BigInteger payloadSequence;

	@Column(name="processed_count")
	private int processedCount;

	public OnAirEntity() {
	}

	public BigInteger getOnairEntityId() {
		return new BigInteger(this.onairEntityId);
	}

	public void setOnairEntityId(String onairEntityId) {
		this.onairEntityId = onairEntityId;
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

}