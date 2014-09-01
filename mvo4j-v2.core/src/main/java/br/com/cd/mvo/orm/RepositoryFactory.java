package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;

public interface RepositoryFactory<F, B, R extends Repository<?, ?>> {

	public static final String BEAN_NAME_PREFIX = RepositoryFactory.class.getName();

	public abstract <T> R getInstance(RepositoryMetaData<T> metaData) throws ConfigurationException;

	B getPersistenceManager(String persistenceManagerFactoryBeanName);

	Class<B> getPersistenceManagagerType();

	Class<? extends Annotation> getEntityAnnotation();

	Class<? extends Annotation> getEntityIdentifierAnnotation();
}
