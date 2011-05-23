package org.meri.simpleshirosecuredapplication.mgt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.Subject;

/**
 * Implementations of this interface are able to disable and enable 
 * user accounts.
 * 
 */
public interface Disabler {

	public abstract boolean isDisabled(AuthenticationInfo info);

	public abstract boolean isDisabled(Subject subject);

	public abstract void disable(Subject subject);

}