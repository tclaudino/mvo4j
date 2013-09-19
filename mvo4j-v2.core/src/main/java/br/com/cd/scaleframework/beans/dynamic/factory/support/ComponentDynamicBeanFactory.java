package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class ComponentDynamicBeanFactory<Config extends BeanConfig<?, ?>, T extends ComponentFactory<Config>>
		extends AbstractDynamicBeanFactory<Config> {

	private Config componentFactory;

	public ComponentDynamicBeanFactory(ComponentFactoryContainer container,
			Config componentFactoryType) {
		super(container);
	}

	@Override
	public Class<Config> getObjectType() {
		return (Class<Config>) componentFactory.getClass();
	}

	@Override
	protected Config getInstanceInternal() throws NoSuchBeanDefinitionException {

		return componentFactory;
	}
}