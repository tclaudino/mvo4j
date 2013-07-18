package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.WebControllerComponent;
import br.com.cd.scaleframework.core.WebControllerComponentDiscoverySupport;
import br.com.cd.scaleframework.core.WebControllerComponentFactory;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.dynamic.WebControllerBean;
import br.com.cd.scaleframework.core.proxy.WebControllerProxy;

public class WebControllerComponentFactoryConfig
		implements
		ComponentFactoryConfig<WebControllerComponent, WebControllerProxy, WebControllerBean, WebControllerComponentFactory, WebControllerComponentDiscoverySupport> {

	@Override
	public WebControllerComponentFactory getComponentFactory() {
		return new DefaultWebControllerComponentFactory();
	}

	@Override
	public WebControllerComponentDiscoverySupport getDiscoverySupport() {
		return new DefaultWebControllerComponentDiscoverySupport();
	}

}
