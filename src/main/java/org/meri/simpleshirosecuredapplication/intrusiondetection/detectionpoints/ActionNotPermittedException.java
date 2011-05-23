package org.meri.simpleshirosecuredapplication.intrusiondetection.detectionpoints;

import org.owasp.appsensor.errors.AppSensorException;
import org.meri.simpleshirosecuredapplication.actions.Actions;

@SuppressWarnings("serial")
public class ActionNotPermittedException extends AppSensorException {

	public static final String EVENT_CODE = "ACTION_NOT_PERMITTED";
	public static final String USER_MESSAGE = "The action is not permitted: ";
	public static final String LOG_MESSAGE = "Attempt to perform an action without permission: ";
	
	public ActionNotPermittedException(Actions action) {
	  super(EVENT_CODE, USER_MESSAGE + action.getName(), LOG_MESSAGE + action.getName());
  }

}
