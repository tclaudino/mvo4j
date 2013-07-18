package br.com.cd.scaleframework.ioc;

import br.com.cd.scaleframework.core.ComponentFactory;

public interface ComponentBeanFactory extends BeanFactoryFacade,
		BeanRegistryFacade {

	void addBeanFactory(BeanFactoryFacade beanFactory);

	void addBeanRegistry(BeanRegistryFacade beanRegistry);

	void registerComponentFactory(
			@SuppressWarnings("rawtypes") ComponentFactory componentFactory);

	// @SuppressWarnings("rawtypes")
	// ComponentProxy getComponent(String componentName);
	//
	// @SuppressWarnings("rawtypes")
	// <O extends ComponentObject, P extends ComponentProxy<O>> P getComponent(
	// Class<?> targetEntity, Class<O> componentType);
	//
	// @SuppressWarnings("rawtypes")
	// <O extends ComponentObject, P extends ComponentProxy<O>> P getComponent(
	// String componentName, Class<O> componentType);
}
