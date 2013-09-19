package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.core.orm.dynamic.ServiceBeanConfig;

public class ServiceComponentFactory implements ComponentFactory {

	@Override
	public boolean isComponentCandidate(DynamicBean<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass()
				.equals(ServiceBeanConfig.class);
	}

	@Override
	public Object getComponent(DynamicBean<?> beanConfig) {

		return null;
	}

}