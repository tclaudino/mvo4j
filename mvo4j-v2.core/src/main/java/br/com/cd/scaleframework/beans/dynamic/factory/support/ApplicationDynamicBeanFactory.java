package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.support.DefaultApplication;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class ApplicationDynamicBeanFactory extends
		AbstractDynamicBeanFactory<Application> {

	public ApplicationDynamicBeanFactory(ComponentFactoryContainer container) {
		super(container);
	}

	@Override
	public Class<Application> getObjectType() {
		return Application.class;
	}

	@Override
	protected Application getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		return new DefaultApplication();
	}
}
