package org.meri.simpleshirosecuredapplication.authc;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimaryPrincipalSameAuthenticationStrategy extends AllSuccessfulStrategy {
	
  private static final Logger log = LoggerFactory.getLogger(PrimaryPrincipalSameAuthenticationStrategy.class);

  @Override
  public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo info, AuthenticationInfo aggregate, Throwable t) {
  	validatePrimaryPrincipals(info, aggregate, realm);
  	return super.afterAttempt(realm, token, info, aggregate, t);
  }
  

	private void validatePrimaryPrincipals(AuthenticationInfo info, AuthenticationInfo aggregate, Realm realm) {
		PrincipalCollection aggregPrincipals = aggregate.getPrincipals();
		if (aggregPrincipals==null || aggregPrincipals.isEmpty() || aggregPrincipals.getPrimaryPrincipal()==null)
			return ;
		
		if (info==null)
			return ;
		
		PrincipalCollection infoPrincipals = info.getPrincipals();
		if (infoPrincipals==null || infoPrincipals.isEmpty() || infoPrincipals.getPrimaryPrincipal()==null) {
			String message = "Primary principal is missing from " + realm.getName() + " result.";
			log.debug(message);
			throw new AuthenticationException(message);
		}
		
		Object aggregPrincipal = aggregPrincipals.getPrimaryPrincipal();
		Object infoPrincipal = infoPrincipals.getPrimaryPrincipal();
		if (!aggregPrincipal.equals(infoPrincipal)) {
			String message = "All realms are required to return the same primary principal. Offending realm: " + realm.getName();
			log.debug(message);
			throw new AuthenticationException(message);
		}
  }

}
