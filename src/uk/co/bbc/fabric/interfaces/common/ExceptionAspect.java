package uk.co.bbc.fabric.interfaces.common;

import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import uk.co.bbc.fabric.interfaces.dao.hibernate.LockManager;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.entity.BaseEntity;
import uk.co.bbc.fabric.interfaces.exception.LockException;
import uk.co.bbc.fabric.interfaces.exception.OnairUpdateException;
@Component
@Aspect
public class ExceptionAspect {
	
	@Pointcut("execution(* uk.co.bbc.fabric.interfaces.services.*.*(..))") 
	private void businessService() {}  
	
	@AfterThrowing(pointcut = "businessService()", throwing = "e")
	  public void myAfterThrowing(JoinPoint joinPoint, Throwable e) {
		String methodName =joinPoint.getSignature().getName();;
		final Logger LOGGER = Logger.getLogger(joinPoint.getSignature().getName()); 
		LOGGER.setLevel(Level.FINEST); 
		LOGGER.addHandler(new ConsoleHandler());
		
		//please release lock 
		
		if(!(e instanceof LockException)){
			LockManager.unlock();
		}
		
		if(e instanceof OnairUpdateException){
			BaseEntity entity = ((OnairUpdateException)e).getEntity();
			String message = "Exception happend while updating onair entity [entity id : "+entity.getOnairEntityId()+"]";
			LOGGER.log(Level.INFO, message);
		}else if(e instanceof LockException){
			LOGGER.log(Level.INFO, e.getMessage());
		}
		LOGGER.log(Level.FINEST, methodName, e);
	  }
}
