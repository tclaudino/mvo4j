package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;
import br.com.cd.scaleframework.core.orm.dynamic.ServiceBeanConfig;

@SuppressWarnings("rawtypes")
public class ServiceComponentFactory extends
		AbstractComponentFactory<ServiceBeanConfig> {

	public ServiceComponentFactory(ComponentFactoryContainer container) {
		super(container, ServiceBeanConfig.class);
	}

	@Override
	public String generateBeanName(
			DynamicBeanManager<ServiceBeanConfig> beanConfig) {

		return ServiceBeanConfig.BEAN_NAME_SUFFIX;
	}

	@Override
	public Class<DynamicBean<?, ?>> createComponentProxy(
			DynamicBeanManager<ServiceBeanConfig> beanConfig)
			throws NoSuchBeanDefinitionException {

		// TODO Auto-generated method stub
		return null;
	}

}