package br.com.cd.scaleframework.orm.suport;

import br.com.cd.scaleframework.orm.RepositoryFactory;

public class NoneSessionFactoryProvider extends
		AbstractSessionFactoryProvider<Void> {

	public NoneSessionFactoryProvider() {
		super(Void.class);
	}

	@Override
	public RepositoryFactory<Void> getRepositoryFactory() {
		throw new UnsupportedOperationException(
				"use this class only in default's attributes values. Replace is required!");
	}

}