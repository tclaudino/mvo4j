package br.com.cd.scaleframework.core.discovery;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public interface ComponentDiscoverySupport<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>> {

	Class<A> getAnnotationType();

	// Class<F> getComponentFactoryType();

	Class<?> getTargetEntity(A annotation);

	String getSessionFactoryQualifier(A annotation);

	O createComponent(String name, String targetEntityClassName,
			ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport);

	O createComponent(A annotation, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport);

	void onRegistryComponent(O component, ComponentBeanFactory beanFactory);

}