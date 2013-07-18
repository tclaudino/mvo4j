package br.com.cd.scaleframework.orm.suport;

import javax.persistence.EntityManagerFactory;

import br.com.cd.scaleframework.orm.RepositoryFactory;

public class JPASessionFactoryProvider extends
		AbstractSessionFactoryProvider<EntityManagerFactory> {

	public JPASessionFactoryProvider() {
		super(EntityManagerFactory.class);
	}

	@Override
	public RepositoryFactory<EntityManagerFactory> getRepositoryFactory() {
		return new JPARepositoryFactory();
	}

}