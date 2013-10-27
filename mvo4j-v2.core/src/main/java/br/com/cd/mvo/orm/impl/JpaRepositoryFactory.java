package br.com.cd.mvo.orm.impl;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;

import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.orm.JpaRepository;
import br.com.cd.mvo.orm.support.AbstractRepositoryFactory;

public class JpaRepositoryFactory
		extends
		AbstractRepositoryFactory<EntityManagerFactory, EntityManager, JpaRepository<?>> {

	public static final Class<? extends Annotation> PERSISTENCE_TYPE_ANNOTATION = Entity.class;
	public static final Class<? extends Annotation> PERSISTENCE_IDENTIFIER_ANNOTATION = Id.class;

	public JpaRepositoryFactory(Container container) {
		super(container, PERSISTENCE_TYPE_ANNOTATION,
				PERSISTENCE_IDENTIFIER_ANNOTATION);
	}

	@Override
	protected EntityManager createPersistenceManager(
			EntityManagerFactory factory) throws NoSuchBeanDefinitionException {

		return factory.createEntityManager();
	}

	@Override
	public JpaRepository<?> getInstance(String persistenceManagerQualifier,
			Class<?> entityClass, RepositoryMetaData metaData) {

		return new JpaRepositoryImpl<>(
				this.getPersistenceManager(persistenceManagerQualifier),
				entityClass, metaData);
	}
}
