package br.com.cd.scaleframework.web.beans.dynamic;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBeanConfig;

public class WebControllerComponentFactory implements ComponentFactory {

	@Override
	public boolean isComponentCandidate(DynamicBean<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass()
				.equals(WebControllerBeanConfig.class);
	}

	@Override
	public Object getComponent(DynamicBean<?> beanConfig) {

		return null;
	}

}