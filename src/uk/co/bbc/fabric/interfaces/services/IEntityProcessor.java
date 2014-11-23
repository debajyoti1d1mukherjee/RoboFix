package uk.co.bbc.fabric.interfaces.services;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

public interface IEntityProcessor<T extends BaseEntity,X,Y> {
	public void processEnitity(T entity)throws OnairUpdateException;
	public Y processAndGetStatus(X param)throws OnairUpdateException;
	
}
