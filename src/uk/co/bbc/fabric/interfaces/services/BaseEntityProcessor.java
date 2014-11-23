package uk.co.bbc.fabric.interfaces.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import uk.co.bbc.fabric.interfaces.dao.hibernate.ConnectionManager;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

public abstract class BaseEntityProcessor<T extends BaseEntity,X,Y> implements IEntityProcessor<T,X,Y>{
	
	public abstract void process(T entity)throws OnairUpdateException;
	public abstract void refresh();
	public abstract Y handle(X param)throws OnairUpdateException;
	
	public void processEnitity(T entity)throws OnairUpdateException{
		process(entity);
	}
	
	public void refreshTempTables(){
		refresh();
	}
	
	public Y processAndGetStatus(X param)throws OnairUpdateException{
		return handle(param);
	}
	
}
