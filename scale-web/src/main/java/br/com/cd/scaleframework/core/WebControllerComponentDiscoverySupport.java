package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.dynamic.WebControllerBean;
import br.com.cd.scaleframework.core.proxy.WebControllerProxy;

public interface WebControllerComponentDiscoverySupport
		extends
		ControllerComponentDiscoverySupport<WebControllerComponent, WebControllerProxy, WebControllerBean, WebControllerComponentFactory> {
}