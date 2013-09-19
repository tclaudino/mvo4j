package br.com.cd.scaleframework.web.beans.dynamic;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.support.AbstractComponentFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBeanConfig;

@SuppressWarnings("rawtypes")
public class WebControllerComponentFactory extends
		AbstractComponentFactory<WebControllerBeanConfig> {

	public WebControllerComponentFactory(ComponentFactoryContainer container) {
		super(container, WebControllerBeanConfig.class);
	}

	@Override
	public String generateBeanName(
			DynamicBeanManager<WebControllerBeanConfig> beanConfig) {

		return WebControllerBeanConfig.BEAN_NAME_SUFFIX;
	}

	@Override
	public Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<WebControllerBeanConfig> beanConfig)
			throws NoSuchBeanDefinitionException {

		// TODO Auto-generated method stub
		return null;
	}

}