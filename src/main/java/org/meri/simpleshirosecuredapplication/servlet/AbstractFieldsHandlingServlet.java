package org.meri.simpleshirosecuredapplication.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Parent of all servlets reading user supplied data. Use {@link #getField(HttpServletRequest, String)}
 * to get fields values.  
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractFieldsHandlingServlet extends HttpServlet {

	public AbstractFieldsHandlingServlet() {
		super();
	}

	/**
	 * Reads sanitized request parameter value from request field.
	 * 
	 * @param request http request
	 * @param parameter name of request parameter 
	 * 
	 * @return request parameter value
	 */
	protected String getField(HttpServletRequest request, String parameter) {
		//FIXME: sanitize it
    return request.getParameter(parameter);
  }

}