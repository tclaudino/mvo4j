package br.com.cd.scaleframework.orm;

import java.io.Serializable;

public interface RepositoryFactory<Factory> {

	public interface SessionFactoryCallback {
		void beforeClose(Object entity);
	}

	<T> Repository<T, Serializable> createRepository(Factory sessionFactory,
			Class<T> entityClass, SessionFactoryCallback callback);
}
