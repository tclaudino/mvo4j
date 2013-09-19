package br.com.cd.scaleframework.web.beans.dynamic;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;

public class WebCrudControllerComponentFactory implements ComponentFactory {

	@Override
	public boolean isComponentCandidate(DynamicBean<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass()
				.equals(WebCrudControllerBeanConfig.class);
	}

	@Override
	public Object getComponent(DynamicBean<?> beanConfig) {

		return null;
	}

}