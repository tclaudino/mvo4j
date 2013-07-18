package br.com.cd.scaleframework.orm.suport;

import org.hibernate.SessionFactory;

import br.com.cd.scaleframework.orm.RepositoryFactory;

public class HibernateSessionFactoryProvider extends
		AbstractSessionFactoryProvider<SessionFactory> {

	public HibernateSessionFactoryProvider() {
		super(SessionFactory.class);
	}

	@Override
	public RepositoryFactory<SessionFactory> getRepositoryFactory() {
		return new HibernateRepositoryFactory();
	}

}