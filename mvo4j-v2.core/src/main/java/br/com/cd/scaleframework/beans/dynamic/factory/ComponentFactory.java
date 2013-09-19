package br.com.cd.scaleframework.beans.dynamic.factory;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public interface ComponentFactory<Config extends BeanConfig<?, ?>> {

	boolean isComponentCandidate(DynamicBeanManager<?> beanConfig);

	String generateBeanName(DynamicBeanManager<Config> beanConfig);

	DynamicBean<?, ?> getDynamicBean(DynamicBeanManager<Config> beanConfig)
			throws NoSuchBeanDefinitionException;

	Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<Config> beanConfig)
			throws NoSuchBeanDefinitionException;

}