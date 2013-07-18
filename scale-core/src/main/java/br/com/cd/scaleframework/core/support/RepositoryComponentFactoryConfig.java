package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.RepositoryComponentDiscoverySupport;
import br.com.cd.scaleframework.core.RepositoryComponentFactory;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.dynamic.RepositoryBean;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;

@SuppressWarnings("rawtypes")
public class RepositoryComponentFactoryConfig
		implements
		ComponentFactoryConfig<RepositoryComponent, RepositoryProxy<?>, RepositoryBean, RepositoryComponentFactory, RepositoryComponentDiscoverySupport> {

	@Override
	public RepositoryComponentFactory getComponentFactory() {
		return new DefaultRepositoryComponentFactory();
	}

	@Override
	public RepositoryComponentDiscoverySupport getDiscoverySupport() {
		return new DefaultRepositoryComponentDiscoverySupport();
	}

}
