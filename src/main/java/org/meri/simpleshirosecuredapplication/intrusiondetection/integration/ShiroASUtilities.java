package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.owasp.appsensor.ASLogger;
import org.owasp.appsensor.ASUser;
import org.owasp.appsensor.ASUtilities;

public class ShiroASUtilities implements ASUtilities {
	
	private final IntrusionDetectionLogger logger = new IntrusionDetectionLogger();

	@Override
	public ASUser getCurrentUser() {
		//acquire logged user from Shiro framework
		Subject subject = SecurityUtils.getSubject();
		//create and return ASUser delegating to Shiro Subject 
		return new ShiroASUser(subject);
	}

	@Override
	public ASLogger getLogger(String className) {
		return logger;
	}

	public HttpServletRequest getCurrentRequest() {
		HttpServletRequest request = AppSensorIntegrationThreadContext.getCurrentRequest();
		return request;
	}

}
