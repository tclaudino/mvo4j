package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.dynamic.RepositoryBean;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;

@SuppressWarnings("rawtypes")
public interface RepositoryComponentFactory
		extends
		ComponentListableFactory<RepositoryComponent, RepositoryProxy<?>, RepositoryBean> {

}
