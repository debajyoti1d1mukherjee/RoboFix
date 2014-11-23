package uk.co.bbc.fabric.interfaces.services;

import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

public interface IEntityHandler {

	public void checkStateAndProcess()throws OnairUpdateException;
}
