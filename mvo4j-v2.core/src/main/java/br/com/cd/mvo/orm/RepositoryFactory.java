package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.config.RepositoryMetaData;

public interface RepositoryFactory<F, B, R extends Repository<?>> {

	public static final String BEAN_NAME_PREFIX = RepositoryFactory.class.getName();

	public abstract <T> R getInstance(RepositoryMetaData<T> metaData);

	B getPersistenceManager(String persistenceManagerFactoryBeanName);

	Class<B> getPersistenceManagagerType();

	Class<? extends Annotation> getEntityAnnotation();

	Class<? extends Annotation> getEntityIdentifierAnnotation();
}
