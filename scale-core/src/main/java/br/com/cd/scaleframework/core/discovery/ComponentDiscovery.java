package br.com.cd.scaleframework.core.discovery;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;

public interface ComponentDiscovery {

	@SuppressWarnings("rawtypes")
	<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> void discover(
			F componentFactory, S discoverySupport,
			ApplicationContext applicationContext,
			ComponentFactorySupport factorySupport,
			ComponentDiscoveryVisitor registryVisitor);

}