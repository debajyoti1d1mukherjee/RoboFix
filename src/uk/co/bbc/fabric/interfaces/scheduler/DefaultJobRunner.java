package uk.co.bbc.fabric.interfaces.scheduler;

import org.springframework.beans.factory.annotation.Autowired;

import uk.co.bbc.fabric.interfaces.exception.LockException;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;
import uk.co.bbc.fabric.interfaces.services.EntityHandler;

public class DefaultJobRunner implements JobRunner {
	
	private EntityHandler entityHandler = null;
	
	/* Method gets invoked in every schedule run. This is the entry point.
	 * 
	 * */
	@Override
	public boolean run() throws OnairUpdateException,LockException{
		System.out.println("DefaultJobRunner:::start run()");
		entityHandler.startProcessing();
		System.out.println("DefaultJobRunner:::end run()");
		return false;
	}

	public EntityHandler getEntityHandler() {
		return entityHandler;
	}
	
	@Autowired
	public void setEntityHandler(EntityHandler entityHandler) {
		this.entityHandler = entityHandler;
	}
}
