package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;

public class MongoDBRepositoryFactory extends AbstractRepositoryFactory<MongoDbFactory, MongoOperations, MongoRepository<?>> {

	public static final Class<? extends Annotation> PERSISTENCE_TYPE_ANNOTATION = Document.class;
	public static final Class<? extends Annotation> PERSISTENCE_IDENTIFIER_ANNOTATION = Id.class;

	public MongoDBRepositoryFactory(Container container) {
		super(container, PERSISTENCE_TYPE_ANNOTATION, PERSISTENCE_IDENTIFIER_ANNOTATION);
	}

	@Override
	protected MongoOperations createPersistenceManager(MongoDbFactory factory) throws NoSuchBeanDefinitionException {

		return new MongoTemplate(factory);
	}

	@Override
	public <T> MongoRepository<T> getInstance(RepositoryMetaData<T> metaData) throws ConfigurationException {

		MongoOperations em = this.getPersistenceManager(metaData.persistenceManagerQualifier());
		MongoRepositoryImpl<T> repositoryImpl = new MongoRepositoryImpl<>(em, metaData);

		return repositoryImpl;
	}

}
