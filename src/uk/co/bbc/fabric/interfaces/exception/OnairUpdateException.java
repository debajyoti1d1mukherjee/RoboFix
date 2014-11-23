package uk.co.bbc.fabric.interfaces.exception;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;

public class OnairUpdateException extends Exception {
	
	BaseEntity entity;
	
	public OnairUpdateException(BaseEntity entity) {
		super();
		this.entity = entity;
	}

	public BaseEntity getEntity() {
		return entity;
	}

	public void setEntity(BaseEntity entity) {
		this.entity = entity;
	}

}
