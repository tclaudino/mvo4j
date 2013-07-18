package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;
import br.com.cd.scaleframework.core.proxy.WebCrudControllerProxy;

public interface WebCrudControllerComponentDiscoverySupport
		extends
		ControllerComponentDiscoverySupport<WebCrudControllerComponent, WebCrudControllerProxy, WebCrudControllerBean, WebCrudControllerComponentFactory> {
}