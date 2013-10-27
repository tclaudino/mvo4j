package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.config.RepositoryMetaData;

public interface RepositoryFactory<F, B, R extends ListenableRepository<?>> {

	public static final String BEAN_NAME_PREFIX = RepositoryFactory.class
			.getName();

	public abstract R getInstance(String persistenceManagerQualifier,
			Class<?> entityClass, RepositoryMetaData metaData);

	B getPersistenceManager(String persistenceManagerFactoryBeanName);

	Class<B> getPersistenceManagagerType();

	Class<? extends Annotation> getEntityAnnotation();

	Class<? extends Annotation> getEntityIdentifierAnnotation();
}
