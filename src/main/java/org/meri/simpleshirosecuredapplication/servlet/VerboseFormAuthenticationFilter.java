package org.meri.simpleshirosecuredapplication.servlet;

import javax.servlet.ServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class VerboseFormAuthenticationFilter extends FormAuthenticationFilter {
	
	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String message = ae.getMessage();
		request.setAttribute(getFailureKeyAttribute(), message);
	}
	
}
