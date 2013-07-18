package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public interface ComponentRegistry<O extends ComponentObject> {

	void registerComponent(O component, ComponentBeanFactory beanFactory);

}
