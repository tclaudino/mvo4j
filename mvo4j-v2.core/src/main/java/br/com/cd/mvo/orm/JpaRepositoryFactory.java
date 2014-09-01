package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;

import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;

public class JpaRepositoryFactory extends AbstractRepositoryFactory<EntityManagerFactory, EntityManager, JpaRepository<?>> {

	public static final Class<? extends Annotation> PERSISTENCE_TYPE_ANNOTATION = Entity.class;
	public static final Class<? extends Annotation> PERSISTENCE_IDENTIFIER_ANNOTATION = Id.class;

	public JpaRepositoryFactory(Container container) {
		super(container, PERSISTENCE_TYPE_ANNOTATION, PERSISTENCE_IDENTIFIER_ANNOTATION);
	}

	@Override
	protected EntityManager createPersistenceManager(EntityManagerFactory factory) throws NoSuchBeanDefinitionException {

		return factory.createEntityManager();
	}

	@Override
	public <T> JpaRepository<T> getInstance(RepositoryMetaData<T> metaData) throws ConfigurationException {

		EntityManager em = this.getPersistenceManager(metaData.persistenceManagerQualifier());
		JpaRepositoryImpl<T> repositoryImpl = new JpaRepositoryImpl<>(em, metaData);

		return repositoryImpl;
	}

}
