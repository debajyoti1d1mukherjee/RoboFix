package uk.co.bbc.fabric.interfaces.scheduler;

import uk.co.bbc.fabric.interfaces.exception.LockException;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;

public interface JobRunner {
	public boolean run()throws OnairUpdateException,LockException;
}
