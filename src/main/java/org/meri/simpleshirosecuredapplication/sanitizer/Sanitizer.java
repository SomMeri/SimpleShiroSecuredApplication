package org.meri.simpleshirosecuredapplication.sanitizer;

import java.io.InputStream;

import org.owasp.appsensor.errors.AppSensorException;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
/**
 * Sanitize user input into XSS attack safe string. The class is thread safe. 
 */
public class Sanitizer {

	//private static final String POLICY_FILE_LOCATION = "/antisamy-tinymce-1.4.4.xml";
	private static final String POLICY_FILE_LOCATION = "/antisamy-slashdot-1.4.4.xml";
	private static Policy policy;

	public Sanitizer() {
	}
	
	public String sanitize(String dirtyInput) {
		AntiSamy as = new AntiSamy();
		try {
	    CleanResults cr = as.scan(dirtyInput, getPolicy());
	    if (cr.getErrorMessages()==null || cr.getErrorMessages().isEmpty()) {
	    	new AppSensorException("eventCode", "userMessage", "logMessage");
	    }
	    return cr.getCleanHTML();
    } catch (ScanException e) {
      throw new RuntimeException(e);
    } catch (PolicyException e) {
      throw new RuntimeException(e);
    }
	}

	private Policy getPolicy() {
		if (policy==null) {
			try {
				InputStream resourceAsStream = getClass().getResourceAsStream(POLICY_FILE_LOCATION);
	      policy = Policy.getInstance(resourceAsStream);
      } catch (PolicyException e) {
	      throw new RuntimeException(e);
      }
		}
  	return policy;
  }
	
	
}
