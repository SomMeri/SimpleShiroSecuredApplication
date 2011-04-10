package org.meri.simpleshirosecuredapplication.servlet;

import java.security.cert.X509Certificate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.meri.simpleshirosecuredapplication.authc.X509CertificateUsernamePasswordToken;

public class CertificateOrFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		return super.isLoginSubmission(request, response) || isCertificateLogInAttempt(request, response);
	}

	private boolean isCertificateLogInAttempt(ServletRequest request, ServletResponse response) {
		return hasCertificate(request) && !getSubject(request, response).isAuthenticated();
	}

	private boolean hasCertificate(ServletRequest request) {
		return null != getCertificate(request);
	}

	private X509Certificate getCertificate(ServletRequest request) {
		X509Certificate[] attribute = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
		return attribute == null ? null : attribute[0];
	}

	@Override
	protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		X509Certificate certificate = getCertificate(request);
		return createToken(username, password, rememberMe, host, certificate);
	}

	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host, X509Certificate certificate) {
		return new X509CertificateUsernamePasswordToken(username, password, rememberMe, host, certificate);
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String message = ae.getMessage();
		request.setAttribute(getFailureKeyAttribute(), message);
	}

}
