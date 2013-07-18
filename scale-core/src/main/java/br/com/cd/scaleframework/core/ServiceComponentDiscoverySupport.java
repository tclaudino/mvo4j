package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.discovery.ComponentDiscoverySupport;
import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.core.proxy.ServiceProxy;

@SuppressWarnings("rawtypes")
public interface ServiceComponentDiscoverySupport
		extends
		ComponentDiscoverySupport<ServiceComponent, ServiceProxy, ServiceBean, ServiceComponentFactory> {
}
