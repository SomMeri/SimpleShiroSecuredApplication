package org.meri.simpleshirosecuredapplication.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.meri.simpleshirosecuredapplication.sanitizer.Sanitizer;
import org.owasp.appsensor.AttackDetectorUtils;

/**
 * Parent of all servlets reading user supplied data. Use
 * {@link #getField(HttpServletRequest, String)} to get fields values.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractFieldsHandlingServlet extends HttpServlet {

	private Sanitizer sanitizer = new Sanitizer();

	public AbstractFieldsHandlingServlet() {
		super();
	}

	/**
	 * Reads sanitized request parameter value from request.
	 * 
	 * @param request
	 *          http request
	 * @param parameter
	 *          name of request parameter
	 * 
	 * @return request parameter value
	 */
	protected String getField(HttpServletRequest request, String parameter) {
		String dirtyValue = request.getParameter(parameter);

		// detection point: XSS, SQL injection
		AttackDetectorUtils.verifySQLInjectionAttack(dirtyValue);
		AttackDetectorUtils.verifyXSSAttack(dirtyValue);

		return sanitizer.sanitize(dirtyValue);
	}

}