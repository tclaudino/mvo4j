package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.orm.SessionFactoryProvider;

public interface RepositoryComponent<C extends RepositoryComponentConfig, F extends SessionFactoryProvider<?>>
		extends ComponentObject<C> {

	F getComponentProvider();

	RepositoryComponent<C, F> getTargetRepository();

	void setTargetRepository(RepositoryComponent<C, F> targetRepository);

}