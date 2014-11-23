package uk.co.bbc.fabric.interfaces.services;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;

public abstract class BaseProcessor<T extends BaseEntity> implements IProcessor<T>{
	
	
	public abstract void process(T entity);
	
	public void processBaseEntity(T entity){
		process(entity);
	}

}
