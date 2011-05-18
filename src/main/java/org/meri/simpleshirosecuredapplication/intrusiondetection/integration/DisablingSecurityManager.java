package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.mgt.SecurityManager;

public interface DisablingSecurityManager extends SecurityManager {

	public void disable(Subject subject);

	public boolean isDisabled(Subject subject);

}