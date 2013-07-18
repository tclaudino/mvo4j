package br.com.cd.scaleframework.core;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.core.discovery.ComponentDiscoverySupport;
import br.com.cd.scaleframework.core.proxy.ControllerProxy;

public interface ControllerComponentDiscoverySupport<O extends ControllerComponent<?>, P extends ControllerProxy<O>, A extends Annotation, F extends ControllerComponentFactory<O, P, A>>
		extends ComponentDiscoverySupport<O, P, A, F> {
}
