package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

@SuppressWarnings("rawtypes")
public class ControllerComponentFactory extends
		AbstractComponentFactory<ControllerBeanConfig> {

	public ControllerComponentFactory(ComponentFactoryContainer container) {
		super(container, ControllerBeanConfig.class);
	}

	@Override
	public boolean isComponentCandidate(DynamicBeanManager<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass()
				.equals(ControllerBeanConfig.class);
	}

	@Override
	public String generateBeanName(
			DynamicBeanManager<ControllerBeanConfig> beanConfig) {

		return beanConfig.getBeanConfig().name()
				+ ControllerBeanConfig.BEAN_NAME_SUFFIX;
	}

	@Override
	public Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<ControllerBeanConfig> beanConfig)
			throws NoSuchBeanDefinitionException {

		// TODO Auto-generated method stub
		return null;
	}

}