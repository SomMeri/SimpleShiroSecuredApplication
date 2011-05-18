package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;

public class DisablingModularRealmAuthenticator extends ModularRealmAuthenticator {

	private final Disabler disabler;
	
	public DisablingModularRealmAuthenticator(Disabler disabler) {
	  super();
	  this.disabler = disabler;
  }

	@Override
  protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
	  AuthenticationInfo info = super.doAuthenticate(authenticationToken);
	  if (disabler.isDisabled(info)) {
	  	throw new AuthenticationException("The account has been disabled.");
	  }
		return info;
  }

}
