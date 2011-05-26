package org.meri.simpleshirosecuredapplication.intrusiondetection.responseaction;

import javax.servlet.http.HttpServletRequest;

import org.owasp.appsensor.APPSENSOR;
import org.owasp.appsensor.ASUtilities;
import org.owasp.appsensor.AppSensorIntrusion;
import org.owasp.appsensor.intrusiondetection.ResponseAction;
import org.owasp.appsensor.intrusiondetection.reference.DefaultResponseAction;

public class CustomResponseAction implements ResponseAction {

	private final ResponseAction delegee = DefaultResponseAction.getInstance();

	@Override
	public boolean handleResponse(String action, AppSensorIntrusion currentIntrusion) {
		if ("warn".equals(action)) {
			Exception securityException = currentIntrusion.getSecurityException();
			String localizedMessage = securityException.getLocalizedMessage();

			ASUtilities asUtilities = APPSENSOR.asUtilities();
			HttpServletRequest request = asUtilities.getCurrentRequest();
			request.setAttribute("securityWarning", localizedMessage);

			return true;
		}

		return delegee.handleResponse(action, currentIntrusion);
	}

}
