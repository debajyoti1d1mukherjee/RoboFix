package uk.co.bbc.fabric.interfaces.services;

import uk.co.bbc.fabric.interfaces.entity.BaseEntity;

public interface IProcessor<T extends BaseEntity> {
	public void process(T entity);

}
