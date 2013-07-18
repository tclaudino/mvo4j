package br.com.cd.scaleframework.core;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public interface ComponentFactory<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation> {

	P getComponent(String componentName, ComponentBeanFactory applicationContext);

	P getComponent(Class<?> targetEntity,
			ComponentBeanFactory applicationContext);

	boolean containsComponent(String componantName);

	boolean containsComponent(Class<?> targetEntity);

	// Class<O> getComponentType();

	List<O> getComponents();

	void setComponents(Map<String, O> components);

}
