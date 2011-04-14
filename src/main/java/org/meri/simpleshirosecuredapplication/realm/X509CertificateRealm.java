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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.shiro.util.Nameable;
import org.apache.xerces.impl.dv.util.Base64;
import org.meri.simpleshirosecuredapplication.authc.X509CertificateAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509CertificateRealm implements Realm, Nameable {

  protected static final String DEFAULT_ACCOUNT_TO_CERTIFICATE_QUERY = "select name from sec_users where serialnumber = ? and issuername = ?";

  private String name;
	private String trustStorePassword;
	private String trustStore;

	protected String jndiDataSourceName;
  protected String accountToCertificateQuery = DEFAULT_ACCOUNT_TO_CERTIFICATE_QUERY;
  protected DataSource dataSource;

	private static final Logger log = LoggerFactory.getLogger(X509CertificateRealm.class);

	@Override
	public boolean supports(AuthenticationToken token) {
		if (token!=null)
			return  token instanceof X509CertificateAuthenticationToken;
		
		return false;
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

		// the issuer name and serial number uniquely identifies certificate
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

	/**
	 * The most simple certificate validation. If we are in web application context, the certificate is already checked 
	 * by web server and valid, so this method is useless there. 
	 *  
	 * @param certificate to be validated
	 * 
	 * @return <code>true</code> if the certificate is valid. <code>false</code> otherwise.
	 */
	private boolean certificateOK(X509Certificate certificate) {
		if (certificate == null)
			return false;

		List<X509Certificate> chain = Arrays.asList(certificate);
		FileInputStream stream = null;

		/* Construct a valid path. */
		try {
			stream = new FileInputStream(getTrustStore());
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(stream, getTrustStorePassword().toCharArray());
			
			X509CertSelector target = new X509CertSelector();
			target.setCertificate(certificate);
			PKIXBuilderParameters params = new PKIXBuilderParameters(keyStore, target);
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

	public String getAccountToCertificateQuery() {
  	return accountToCertificateQuery;
  }

	public void setAccountToCertificateQuery(String accountToCertificateQuery) {
  	this.accountToCertificateQuery = accountToCertificateQuery;
  }

	public String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public void setJndiDataSourceName(String jndiDataSourceName) {
		this.jndiDataSourceName = jndiDataSourceName;
		this.dataSource = getDataSourceFromJNDI(jndiDataSourceName);
	}

	private DataSource getDataSourceFromJNDI(String jndiDataSourceName) {
		try {
			InitialContext ic = new InitialContext();
			return (DataSource) ic.lookup(jndiDataSourceName);
		} catch (NamingException e) {
			log.error("JNDI error while retrieving " + jndiDataSourceName, e);
			throw new AuthorizationException(e);
		}
	}

	private String findUsernameToCertificate(String issuerName, BigInteger serialNumber) {
		String base64Serial = Base64.encode(serialNumber.toByteArray());
		return getAccountName(issuerName, base64Serial);
	}

	private String getAccountName(String issuerName, String base64Serial) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			statement = conn.prepareStatement(accountToCertificateQuery);
			statement.setString(1, base64Serial);
			statement.setString(2, issuerName);

			resultSet = statement.executeQuery();

			boolean hasAccount = resultSet.next();
			if (!hasAccount)
				return null;

			String username = resultSet.getString(1);

			if (resultSet.next()) {
				throw new AuthenticationException("More than one account for thre certificate.");
			}

			return username;
		} catch (SQLException e) {
			final String message = "There was a SQL error while authenticating user.";
			if (log.isErrorEnabled()) {
				log.error(message, e);
			}
			throw new AuthenticationException(message, e);
			
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(conn);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
