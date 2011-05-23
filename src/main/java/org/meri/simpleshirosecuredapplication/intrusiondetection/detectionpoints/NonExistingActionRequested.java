package org.meri.simpleshirosecuredapplication.intrusiondetection.detectionpoints;

import org.owasp.appsensor.errors.AppSensorException;

@SuppressWarnings("serial")
public class NonExistingActionRequested extends AppSensorException {

	public static final String EVENT_CODE = "ACTION_DOES_NOT_EXISTS";
	public static final String USER_MESSAGE = "The action does not exists: ";
	public static final String LOG_MESSAGE = "Attempt to perform non-existing action: ";
	
	public NonExistingActionRequested(String action) {
	  super(EVENT_CODE, USER_MESSAGE + action, LOG_MESSAGE + action);
  }

}
