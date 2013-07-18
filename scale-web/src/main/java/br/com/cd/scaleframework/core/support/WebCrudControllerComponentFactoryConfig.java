package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.WebCrudControllerComponent;
import br.com.cd.scaleframework.core.WebCrudControllerComponentDiscoverySupport;
import br.com.cd.scaleframework.core.WebCrudControllerComponentFactory;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;
import br.com.cd.scaleframework.core.proxy.WebCrudControllerProxy;

@SuppressWarnings("rawtypes")
public class WebCrudControllerComponentFactoryConfig
		implements
		ComponentFactoryConfig<WebCrudControllerComponent, WebCrudControllerProxy, WebCrudControllerBean, WebCrudControllerComponentFactory, WebCrudControllerComponentDiscoverySupport> {

	@Override
	public WebCrudControllerComponentFactory getComponentFactory() {
		return new DefaultWebCrudControllerComponentFactory();
	}

	@Override
	public WebCrudControllerComponentDiscoverySupport getDiscoverySupport() {
		return new DefaultWebCrudControllerComponentDiscoverySupport();
	}

}
