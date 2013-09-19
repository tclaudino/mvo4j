package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.dynamic.CrudControllerBeanConfig;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

@SuppressWarnings("rawtypes")
public class CrudControllerComponentFactory extends
		AbstractComponentFactory<CrudControllerBeanConfig> {

	public CrudControllerComponentFactory(ComponentFactoryContainer container) {
		super(container, CrudControllerBeanConfig.class);
	}

	@Override
	public String generateBeanName(
			DynamicBeanManager<CrudControllerBeanConfig> beanConfig) {

		return CrudControllerBeanConfig.BEAN_NAME_SUFFIX;
	}

	@Override
	public Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<CrudControllerBeanConfig> beanConfig)
			throws NoSuchBeanDefinitionException {

		// TODO Auto-generated method stub
		return null;
	}

}