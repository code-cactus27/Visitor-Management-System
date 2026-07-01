package com.infy.visitormanagement.utility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.infy.visitormanagement.exception.VisitorManagementException;
@Aspect
@Component
public class LoggingAspect {
	private static final Log LOGGER=LogFactory.getLog(LoggingAspect.class);
	    @AfterThrowing(pointcut = "execution(* com.infy.visitormanagement.service.*Impl.*(..))",throwing = "exception")
	    public void logServiceException(VisitorManagementException exception)
	    {
		LOGGER.error(exception.getMessage(),exception); 
	    }
}
