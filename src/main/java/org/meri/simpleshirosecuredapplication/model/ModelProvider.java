package org.meri.simpleshirosecuredapplication.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.shiro.SecurityUtils;

public class ModelProvider {
	
	private EntityManager em;
	
	/**
	 * @return current user data from database. If there is no such line, return null.
	 */
	public UserPersonalData getCurrentUserData() {
		EntityManager em = getEntityManager();
		String loggedUser = (String)SecurityUtils.getSubject().getPrincipal();
		UserPersonalData loggedUserData = em.find(UserPersonalData.class, loggedUser);
		return loggedUserData;
	}

	private EntityManager getEntityManager() {
		if (!hasActiveEntityManager()) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleShiroSecuredApplicationPU");
			em = emf.createEntityManager();
		}
	  return em;
  }
	
	public void close() {
		if (hasActiveEntityManager()) {
			em.close();
		}
	}

	private boolean hasActiveEntityManager() {
	  return em!=null && em.isOpen();
  }

	public void beginTransaction() {
		getEntityManager().getTransaction().begin();
  }

	public void persist(Object entity) {
	  getEntityManager().persist(entity);
  }

	public void commit() {
		getEntityManager().getTransaction().commit();
  }
}
