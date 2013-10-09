package br.cd.mvo4j.test.multitenancy.internal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class InfraManager {

	@PersistenceContext(name = "INFRA_DS")
	EntityManager em;

	public String determineUserDataSource(String user) {
		String qry = "SELECT TARGET_ALIAS FROM USER_DATASOURCES WHERE USER = ?1";
		Query query = em.createNativeQuery(qry);
		query.setParameter(1, user);
		return (String) query.getSingleResult();
	}

}
