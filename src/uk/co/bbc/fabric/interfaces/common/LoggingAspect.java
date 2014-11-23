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
@Component
@Aspect
public class LoggingAspect {
	
	@Around("businessService()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
		final Logger LOGGER = Logger.getLogger(joinPoint.getSignature().getName()); 
		LOGGER.setLevel(Level.FINEST); 
		LOGGER.addHandler(new ConsoleHandler());
		LOGGER.log(LOGGER.getLevel(), "Class Name : "+joinPoint.getTarget().getClass());
		LOGGER.log(LOGGER.getLevel(), "method start : "+joinPoint.getSignature().getName());
		LOGGER.log(LOGGER.getLevel(), "method arguments : "+Arrays.toString(joinPoint.getArgs()));
		Object retVal =joinPoint.proceed(); //continue on the intercepted method
		LOGGER.log(LOGGER.getLevel(),"method end!");
		return retVal;
	}
	
	@Pointcut("execution(* uk.co.bbc.fabric.interfaces.services.*.*(..))") 
	private void businessService() {}  
	
}
