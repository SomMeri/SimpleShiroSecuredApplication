package org.meri.simpleshirosecuredapplication.intrusiondetection.integration;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.subject.Subject;

/**
 * Poor mans disabler service which keeps list of all disabled accounts 
 * in cache.  
 * 
 */
public class InMemoryDisabler implements CacheManagerAware, Disabler {

	private static final String DISABLED_ACCOUNTS_CACHE = DisablingModularRealmAuthenticator.class.getName() + ".disabled";
	private CacheManager cacheManager;

	public InMemoryDisabler() {
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
  public boolean isDisabled(AuthenticationInfo info) {
  	Cache<Object, Object> cache = getCache();
  	Object isDisabled = cache.get(info.getPrincipals().getPrimaryPrincipal());
  	
    return isDisabled!=null;
  }

	@Override
  public boolean isDisabled(Subject subject) {
  	Cache<Object, Object> cache = getCache();
  	Object isDisabled = cache.get(subject.getPrincipal());
  	
    return isDisabled!=null;
  }

	@Override
  public void disable(Subject subject) {
  	Object principal = subject.getPrincipal();
  	getCache().put(principal, principal);
  }

	private Cache<Object, Object> getCache() {
	  return cacheManager.getCache(InMemoryDisabler.DISABLED_ACCOUNTS_CACHE);
  }

}