package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;

public class ControllerComponentFactory implements ComponentFactory {

	@Override
	public boolean isComponentCandidate(DynamicBean<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass()
				.equals(ControllerBeanConfig.class);
	}

	@Override
	public Object getComponent(DynamicBean<?> beanConfig) {

		return null;
	}

}