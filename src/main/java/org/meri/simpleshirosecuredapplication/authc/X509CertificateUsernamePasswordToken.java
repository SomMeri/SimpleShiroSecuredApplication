package org.meri.simpleshirosecuredapplication.authc;

import java.security.cert.X509Certificate;

import org.apache.shiro.authc.UsernamePasswordToken;

@SuppressWarnings("serial")
public class X509CertificateUsernamePasswordToken extends UsernamePasswordToken implements X509CertificateAuthenticationToken {

	private X509Certificate certificate;
	
	public X509CertificateUsernamePasswordToken() {
	  super();
  }

	public X509CertificateUsernamePasswordToken(String username, String password) {
	  super(username, password);
  }
	
	public X509CertificateUsernamePasswordToken(X509Certificate certificate) {
	  super();
	  this.certificate = certificate;
  }

	public X509CertificateUsernamePasswordToken(String username, String password, X509Certificate certificate) {
	  super(username, password);
	  this.certificate = certificate;
  }

	public X509CertificateUsernamePasswordToken(String username, String password, boolean rememberMe, String host, X509Certificate certificate) {
	  super(username, password, rememberMe, host);
	  this.certificate = certificate;
  }

	@Override
	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
  	this.certificate = certificate;
  }

}
