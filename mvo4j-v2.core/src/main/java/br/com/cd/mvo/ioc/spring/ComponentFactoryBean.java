package br.com.cd.mvo.ioc.spring;

import org.springframework.beans.factory.FactoryBean;

import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;

public class ComponentFactoryBean<T> implements FactoryBean<T> {

	private ComponentFactory<T> componentFactory;

	public ComponentFactoryBean(ComponentFactory<T> container) {
		this.componentFactory = container;
	}

	@Override
	public T getObject() {
		try {
			return componentFactory.getInstance();
		} catch (NoSuchBeanDefinitionException e) {
			throw new org.springframework.beans.factory.NoSuchBeanDefinitionException(this.getObjectType());
		}
	}

	@Override
	public Class<T> getObjectType() {
		return componentFactory.getComponentType();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
