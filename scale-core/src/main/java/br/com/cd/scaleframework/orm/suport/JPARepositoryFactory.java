package br.com.cd.scaleframework.orm.suport;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;

import br.com.cd.scaleframework.orm.Repository;
import br.com.cd.scaleframework.orm.RepositoryFactory;

public class JPARepositoryFactory implements
		RepositoryFactory<EntityManagerFactory> {

	@Override
	public <T> Repository<T, Serializable> createRepository(
			EntityManagerFactory sessionFactory,
			final Class<T> entityClass,
			final br.com.cd.scaleframework.orm.RepositoryFactory.SessionFactoryCallback callback) {

		// return new AbstractJpaRepository<T, Serializable>(sessionFactory) {
		//
		// @Override
		// protected void beforeClose(Object entity) {
		// callback.beforeClose(entity);
		// }
		//
		// @Override
		// protected final Class<T> getEntityClass() {
		// return entityClass;
		// }
		// };

		return null;
	}

}
