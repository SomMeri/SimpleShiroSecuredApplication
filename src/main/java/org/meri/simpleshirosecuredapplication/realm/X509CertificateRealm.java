package org.meri.simpleshirosecuredapplication.realm;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.Nameable;
import org.meri.simpleshirosecuredapplication.authc.X509CertificateAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509CertificateRealm implements Realm, Nameable {

  private String name;
	private String trustStorePassword;
	private String trustStore;

	private static final Logger log = LoggerFactory.getLogger(X509CertificateRealm.class);

	@Override
	public boolean supports(AuthenticationToken token) {
		return token != null && X509CertificateAuthenticationToken.class.isAssignableFrom(token.getClass());
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// the cast is legal, since Shiro will let in only X509CertificateAuthenticationToken tokens
		X509CertificateAuthenticationToken certificateToken = (X509CertificateAuthenticationToken) token;
		X509Certificate certificate = certificateToken.getCertificate();
		
		// verify certificate
		if (!certificateOK(certificate)) {
			return null;
		}

		// the issuer name and serial number identify a unique certificate
		BigInteger serialNumber = certificate.getSerialNumber();
		String issuerName = certificate.getIssuerDN().getName();

		// find account associated with certificate
		String username = findUsernameToCertificate(issuerName, serialNumber);
		if (username == null) {
			// return null as no account was found
			return null;
		}

		// sucesfull verification, return authentication info
		return new SimpleAuthenticationInfo(username, certificate, getName());
	}

	private boolean certificateOK(X509Certificate certificate) {
		// we assume that certificate is already checked by web server and valid
		if (certificate == null)
			return false;

		List<X509Certificate> chain = Arrays.asList(certificate);
		FileInputStream stream = null;

		/* Construct a valid path. */
		try {
			stream = new FileInputStream(getTrustStore());
			KeyStore anchors = KeyStore.getInstance(KeyStore.getDefaultType());
			anchors.load(stream, getTrustStorePassword().toCharArray());
			
			X509CertSelector target = new X509CertSelector();
			target.setCertificate(certificate);
			PKIXBuilderParameters params = new PKIXBuilderParameters(anchors, target);
			CertStoreParameters intermediates = new CollectionCertStoreParameters(chain);
			params.addCertStore(CertStore.getInstance("Collection", intermediates));

			// Lets ignore certificate revocation list for example purposes
			Collection<X509CRL> crls = Collections.emptyList();
			CertStoreParameters revoked = new CollectionCertStoreParameters(crls);
			params.addCertStore(CertStore.getInstance("Collection", revoked));

			// If build() returns successfully, the certificate is valid.
			@SuppressWarnings("unused")
			CertPathBuilderResult r = CertPathBuilder.getInstance("PKIX").build(params);
		} catch (Exception ex) {
			log.debug("Certificate validation failed. ", ex);
			return false;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				log.error("Could not close truststore stream.", e);
			}
		}

		return true;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public String getTrustStore() {
		return trustStore;
	}

	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}

	private String findUsernameToCertificate(String issuerName, BigInteger serialNumber) {
		// TODO replace dummy method by something real
		return "servicessales";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
