package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.discovery.ComponentDiscoverySupport;
import br.com.cd.scaleframework.core.dynamic.RepositoryBean;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;

@SuppressWarnings("rawtypes")
public interface RepositoryComponentDiscoverySupport
		extends
		ComponentDiscoverySupport<RepositoryComponent, RepositoryProxy<?>, RepositoryBean, RepositoryComponentFactory> {
}
