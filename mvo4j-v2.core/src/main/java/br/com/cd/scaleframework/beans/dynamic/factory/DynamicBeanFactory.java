package br.com.cd.scaleframework.beans.dynamic.factory;

import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public interface DynamicBeanFactory<T> {

	T getInstance() throws NoSuchBeanDefinitionException;

	Class<T> getObjectType();
}
