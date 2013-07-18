package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.ServiceComponentDiscoverySupport;
import br.com.cd.scaleframework.core.ServiceComponentFactory;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.core.proxy.ServiceProxy;

@SuppressWarnings("rawtypes")
public class ServiceComponentFactoryConfig
		implements
		ComponentFactoryConfig<ServiceComponent, ServiceProxy, ServiceBean, ServiceComponentFactory, ServiceComponentDiscoverySupport> {

	@Override
	public ServiceComponentFactory getComponentFactory() {
		return new DefaultServiceComponentFactory();
	}

	@Override
	public ServiceComponentDiscoverySupport getDiscoverySupport() {
		return new DefaultServiceComponentDiscoverySupport();
	}

}
