package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;

@SuppressWarnings("rawtypes")
public interface ComponentFactory<T> extends Comparable<ComponentFactory> {

	T getInstance() throws NoSuchBeanDefinitionException;

	Class<T> getComponentType();

	int getOrder();

}
