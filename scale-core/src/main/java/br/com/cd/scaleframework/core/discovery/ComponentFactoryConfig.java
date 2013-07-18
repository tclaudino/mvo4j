package br.com.cd.scaleframework.core.discovery;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;

@SuppressWarnings("rawtypes")
public interface ComponentFactoryConfig<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> {

	F getComponentFactory();

	S getDiscoverySupport();

}