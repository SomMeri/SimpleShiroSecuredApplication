package org.meri.simpleshirosecuredapplication.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.meri.simpleshirosecuredapplication.actions.Actions;

public class PerformFunctionAndGoBackServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = -7896114563632467947L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String actionResult = performAction(action);
		request.setAttribute("actionResultMessage", actionResult);

		// forward the request and response back to original page
		String originalPage = request.getParameter("originalPage");
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(originalPage);
		dispatcher.forward(request, response);
	}

	private String performAction(String actionName) {
		Actions result = findAction(actionName);
		return result==null ? null : result.doIt();
  }

	private Actions findAction(String actionName) {
		if (actionName==null)
			return null;
		
	  Actions[] values = Actions.values();
		
		for (Actions action : values) {
			if (actionName.equals(action.getName()))
	    return action;
    }
	  return null;
  }

	public String getUrl(HttpServletRequest request) {
		String reqUrl = request.getRequestURL().toString();
		String queryString = request.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

}
