package br.com.cd.scaleframework.web.beans.dynamic;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.support.AbstractComponentFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;

@SuppressWarnings("rawtypes")
public class WebCrudControllerComponentFactory extends
		AbstractComponentFactory<WebCrudControllerBeanConfig> {

	public WebCrudControllerComponentFactory(ComponentFactoryContainer container) {
		super(container, WebCrudControllerBeanConfig.class);
	}

	@Override
	public String generateBeanName(
			DynamicBeanManager<WebCrudControllerBeanConfig> beanConfig) {

		return WebCrudControllerBeanConfig.BEAN_NAME_SUFFIX;
	}

	@Override
	public Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<WebCrudControllerBeanConfig> beanConfig)
			throws NoSuchBeanDefinitionException {

		// TODO Auto-generated method stub
		return null;
	}

}