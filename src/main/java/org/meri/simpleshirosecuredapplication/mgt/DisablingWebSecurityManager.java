package org.meri.simpleshirosecuredapplication.mgt;

import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

public class DisablingWebSecurityManager extends DefaultWebSecurityManager implements DisablingSecurityManager {

	private final Disabler disabler;

	public DisablingWebSecurityManager() {
		disabler = new InMemoryDisabler();
		setAuthenticator(new DisablingModularRealmAuthenticator(disabler));
	}

	@Override
	public void afterCacheManagerSet() {
		super.afterCacheManagerSet();
		applyCacheManagerToOwnedObjects();
	}

	private void applyCacheManagerToOwnedObjects() {
		if (disabler instanceof CacheManagerAware) {
			((CacheManagerAware) disabler).setCacheManager(getCacheManager());
		}
	}

	@Override
	public void disable(Subject subject) {
		disabler.disable(subject);
	}

	@Override
  public boolean isDisabled(Subject subject) {
	  return disabler.isDisabled(subject);
  }

}
