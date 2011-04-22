package org.meri.simpleshirosecuredapplication.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.shiro.SecurityUtils;

public class ModelProvider {
	
	private EntityManager em;
	
	/**
	 * Find data about logged user.
	 * 
	 * @return current user data from database. If there is no such line, return null.
	 */
	public UserPersonalData getCurrentUserData() {
		EntityManager em = getEntityManager();
		String loggedUser = (String)SecurityUtils.getSubject().getPrincipal();
		UserPersonalData loggedUserData = em.find(UserPersonalData.class, loggedUser);
		return loggedUserData;
	}
	
	/**
	 * Find data about all users.
	 * 
	 * @return users data from database. 
	 */	public List<UserPersonalData> getAllUsersData() {
		EntityManager em = getEntityManager();
		TypedQuery<UserPersonalData> query = em.createQuery("SELECT x FROM UserPersonalData x",UserPersonalData.class);
		List<UserPersonalData> result = query.getResultList();
		return result;
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
