package br.com.cd.scaleframework.orm.suport;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import br.com.cd.scaleframework.orm.Repository;
import br.com.cd.scaleframework.orm.RepositoryFactory;
import br.com.cd.scaleframework.orm.hibernate.AbstractHibernateRepository;

public class HibernateRepositoryFactory implements
		RepositoryFactory<SessionFactory> {

	@Override
	public <T> Repository<T, Serializable> createRepository(
			SessionFactory sessionFactory,
			final Class<T> entityClass,
			final br.com.cd.scaleframework.orm.RepositoryFactory.SessionFactoryCallback callback) {

		return new AbstractHibernateRepository<T, Serializable>(sessionFactory) {

			@Override
			protected void beforeClose(Object entity) {
				callback.beforeClose(entity);
			}

			@Override
			protected final Class<T> getEntityClass() {
				return entityClass;
			}
		};
	}

}
