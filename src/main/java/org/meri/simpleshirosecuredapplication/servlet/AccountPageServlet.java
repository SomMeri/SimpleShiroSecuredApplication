package org.meri.simpleshirosecuredapplication.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.meri.simpleshirosecuredapplication.model.ModelProvider;
import org.meri.simpleshirosecuredapplication.model.UserPersonalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class AccountPageServlet extends AbstractFieldsHandlingServlet {
	
	private static final Logger log = LoggerFactory.getLogger(AccountPageServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loggedPrincipal = (String) SecurityUtils.getSubject().getPrincipal();
		String firstname = getField(request, "firstname");
		String lastname = getField(request, "lastname");
		String about = getField(request, "about");

		try {
			saveData(loggedPrincipal, firstname, lastname, about);
			request.setAttribute(ServletConstants.actionResultMessage, "Saved was successful.");
		} catch (Exception ex) {
			log.error("Could not save data.", ex);
			request.setAttribute(ServletConstants.actionResultMessage, "Saved unsuccessful :(.");
		}

		// forward the request and response back to original page
		String originalPage = request.getParameter(ServletConstants.originalPage);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(originalPage);
		dispatcher.forward(request, response);
	}

	private void saveData(String loggedPrincipal, String firstname, String lastname, String about) {
		ModelProvider mp = new ModelProvider();
		try {
			UserPersonalData data = mp.getCurrentUserData();
			mp.beginTransaction();
			if (data == null) {
				data = new UserPersonalData();
				updateUser(data, loggedPrincipal, firstname, lastname, about);
				mp.persist(data);
			} else {
				updateUser(data, loggedPrincipal, firstname, lastname, about);
			}
			mp.commit();
		} finally {
			mp.close();
		}
	}

	private void updateUser(UserPersonalData data, String loggedPrincipal, String firstname, String lastname, String about) {
		data.setUserName(loggedPrincipal);
		data.setFirstname(firstname);
		data.setLastname(lastname);
		data.setAbout(about);
	}

}
