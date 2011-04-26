package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.subject.Subject;
import org.owasp.appsensor.ASUser;

public class ShiroASUser implements ASUser {

	private final Subject subject;

	public ShiroASUser(Subject subject) {
		super();
		this.subject = subject;
	}

	@Override
	public long getAccountId() {
		Object principal = subject.getPrincipal();
		return principal==null ? 0 : principal.hashCode();
	}

	@Override
	public String getAccountName() {
		Object primaryPrincipal = subject.getPrincipal();
		if (primaryPrincipal == null)
			return null;

		Object property = null;

		try {
			property = PropertyUtils.getProperty(primaryPrincipal, "name");
		} catch (Exception e) {
		}
		return property == null ? primaryPrincipal.toString() : property.toString();
	}

	@Override
	public boolean isAnonymous() {
		return subject.getPrincipal()==null;
	}

	@Override
	public void logout() {
		subject.logout();
	}

	@Override
	public void disable() {
		throw new UnsupportedOperationException("Not implemeted yet.");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
