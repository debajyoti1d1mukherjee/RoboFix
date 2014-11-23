package uk.co.bbc.fabric.interfaces.derby.entity;

import java.io.Serializable;

import javax.persistence.*;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the EPISODE database table.
 * 
 */
@Entity
@Table(name="episode")
public class Episode extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created")
	private Date created;
	
	@Column(name="entitystatus")
	private String entitystatus;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastprocessed")
	private Date lastprocessed;
	
	@Id
	@Column(name="onairentityid")
	private long onairentityid;
	
	@Column(name="seriesid")
	private BigDecimal seriesid;
	
	@Column(name="type")
	private String type;
	
	@Column(name="uid")
	private String uid;
	
	@Column(name="episodeReleased")
	private String episodeReleased = "N";
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="releaseTime")
	private Date releaseTime;

	@Column(name="errorMessage")
	public String errorMessage=null;
	
	@Column(name="dryRun")
	private String dryRun;

	public String getDryRun() {
		return dryRun;
	}

	public void setDryRun(String dryRun) {
		this.dryRun = dryRun;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getEpisodeReleased() {
		return episodeReleased;
	}

	public void setEpisodeReleased(String episodeReleased) {
		this.episodeReleased = episodeReleased;
	}

	public Episode() {
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getEntityStatus() {
		return this.entitystatus;
	}

	public void setEntitystatus(String entitystatus) {
		this.entitystatus = entitystatus;
	}

	public Date getLastprocessed() {
		return this.lastprocessed;
	}

	public void setLastprocessed(Date lastprocessed) {
		this.lastprocessed = lastprocessed;
	}

	public long getOnairentityid() {
		return this.onairentityid;
	}

	public void setOnairentityid(long onairentityid) {
		this.onairentityid = onairentityid;
	}

	public BigDecimal getSeriesid() {
		return this.seriesid;
	}

	public void setSeriesid(BigDecimal seriesid) {
		this.seriesid = seriesid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public BigInteger getOnairEntityId() {
		// TODO Auto-generated method stub
		return new BigInteger(getOnairentityid()+"");
	}

}