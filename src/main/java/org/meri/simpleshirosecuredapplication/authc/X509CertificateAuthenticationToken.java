package org.meri.simpleshirosecuredapplication.authc;

import java.security.cert.X509Certificate;

import org.apache.shiro.authc.AuthenticationToken;

public interface X509CertificateAuthenticationToken extends AuthenticationToken {

	public abstract X509Certificate getCertificate();

}