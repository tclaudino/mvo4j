package br.com.cd.mvo.orm.impl;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.orm.JpaRepository;
import br.com.cd.mvo.orm.PersistenceManagerFactory;

public class JpaPersistenceManagerFactory
		extends
		PersistenceManagerFactory<EntityManagerFactory, EntityManager, JpaRepository<?>> {

	public static final Class<? extends Annotation> PERSISTENCE_IDENTIFIER_ANNOTATION = Id.class;
	public static final Class<? extends Annotation> PERSISTENCE_TYPE_ANNOTATION = Entity.class;

	public JpaPersistenceManagerFactory(Container container) {
		super(container, PERSISTENCE_IDENTIFIER_ANNOTATION,
				PERSISTENCE_IDENTIFIER_ANNOTATION);
	}

	@Override
	protected EntityManager getInstance(EntityManagerFactory factory)
			throws NoSuchBeanDefinitionException {

		return factory.createEntityManager();
	}

	@Override
	public JpaRepository<?> getRepositoryInstance(
			String persistenceManagerQualifier, Class<?> entityClass) {
		return new JpaRepositoryImpl<>(
				this.getInstance(persistenceManagerQualifier), entityClass);
	}
}
