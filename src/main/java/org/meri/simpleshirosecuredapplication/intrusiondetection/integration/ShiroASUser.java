package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
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
		SecurityManager securityManager = SecurityUtils.getSecurityManager();
		
		if (securityManager instanceof DisablingSecurityManager) {
			DisablingSecurityManager disablingManager =  (DisablingSecurityManager) securityManager;
			disablingManager.disable(subject);
		}

		subject.logout();
	}

	@Override
	public boolean isEnabled() {
		SecurityManager securityManager = SecurityUtils.getSecurityManager();
		
		if (securityManager instanceof DisablingSecurityManager) {
			DisablingSecurityManager disablingManager =  (DisablingSecurityManager) securityManager;
			return disablingManager.isDisabled(subject);
		}
		return true;
	}

}
