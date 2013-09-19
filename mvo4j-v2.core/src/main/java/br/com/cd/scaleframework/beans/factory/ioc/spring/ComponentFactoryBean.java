package br.com.cd.scaleframework.beans.factory.ioc.spring;

import org.springframework.beans.factory.FactoryBean;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanFactory;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class ComponentFactoryBean<T> implements FactoryBean<T> {

	private DynamicBeanFactory<T> container;

	public ComponentFactoryBean(DynamicBeanFactory<T> container) {
		this.container = container;

		// this.targetType = GenericsUtils.getTypeFor(factoryType,
		// ComponentFactory.class);
	}

	@Override
	public T getObject() {
		try {
			return container.getInstance();
		} catch (NoSuchBeanDefinitionException e) {
			throw new org.springframework.beans.factory.NoSuchBeanDefinitionException(
					this.getObjectType());
		}
	}

	@Override
	public Class<T> getObjectType() {
		return container.getObjectType();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
