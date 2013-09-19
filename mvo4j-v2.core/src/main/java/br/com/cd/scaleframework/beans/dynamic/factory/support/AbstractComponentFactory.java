package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public abstract class AbstractComponentFactory<Config extends BeanConfig<?, ?>>
		implements ComponentFactory<Config> {

	protected ComponentFactoryContainer container;
	private Class<Config> configType;

	public AbstractComponentFactory(ComponentFactoryContainer container,
			Class<Config> config) {
		this.container = container;
		this.configType = config;
	}

	@Override
	public boolean isComponentCandidate(DynamicBeanManager<?> beanConfig) {
		return beanConfig.getBeanConfig().getClass().equals(configType);
	}

	@Override
	public DynamicBean<?, ?> getDynamicBean(
			DynamicBeanManager<Config> beanConfig)
			throws NoSuchBeanDefinitionException {

		String beanName = this.generateBeanName(beanConfig);

		if (!container.containsBean(beanName)) {
			container.registerBean(beanName,
					this.createComponentProxy(beanConfig));
		}

		return container.getBean(beanName, DynamicBean.class);
	}

}