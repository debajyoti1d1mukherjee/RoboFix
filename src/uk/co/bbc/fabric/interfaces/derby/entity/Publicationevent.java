package uk.co.bbc.fabric.interfaces.derby.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;

import java.util.Date;


/**
 * The persistent class for the PUBLICATIONEVENT database table.
 * 
 */
@Entity
@Table(name="publicationevent")
public class Publicationevent extends BaseEntity implements Comparable<Publicationevent>, Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created")
	private Date created;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="releaseTime")
	private Date releaseTime;

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	
	@Column(name="dryRun")
	private String dryRun;

	public String getDryRun() {
		return dryRun;
	}

	public void setDryRun(String dryRun) {
		this.dryRun = dryRun;
	}
	
	@Column(name="errorMessage")
	public String errorMessage=null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name="entitystatus")
	private String entitystatus;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastprocessed")
	private Date lastprocessed;

	@Id
	@Column(name="onairentityid")
	private long onairentityid;

	@Column(name="uid")
	private String uid;
	
	@Column(name="processedstate")
	private String processedstate = null;
	
	@Column(name="episodecompleted")
	private String episodecompleted = "N";
	
	@Column(name="pvreleased")
	private String pvreleased = "N";

	public Publicationevent() {
	}

	public String getProcessedstate() {
		return processedstate;
	}

	public void setProcessedstate(String processedstate) {
		this.processedstate = processedstate;
	}

	public String getEpisodecompleted() {
		return episodecompleted;
	}

	public void setEpisodecompleted(String episodecompleted) {
		this.episodecompleted = episodecompleted;
	}

	public String getPvreleased() {
		return pvreleased;
	}

	public void setPvreleased(String pvreleased) {
		this.pvreleased = pvreleased;
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
	
	@Override
	public int compareTo(Publicationevent o) {
		int retVal = 0;
		if(getLastprocessed().equals(o.getLastprocessed())){
			retVal= 0;
		}else if(getLastprocessed().before(o.getLastprocessed())){
			retVal= -1;
		}
		else if(getLastprocessed().after(o.getLastprocessed())){
			retVal= 1;
		}
		return retVal;
	}
}