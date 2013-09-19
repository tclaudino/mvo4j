package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.Collection;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public abstract class AbstractDynamicBeanFactory<T> implements
		DynamicBeanFactory<T> {

	private ComponentFactoryContainer container;

	public AbstractDynamicBeanFactory(ComponentFactoryContainer container) {
		this.container = container;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final T getInstance() throws NoSuchBeanDefinitionException {

		try {
			return container.getBean(this.getObjectType());
		} catch (NoSuchBeanDefinitionException e) {

			try {

				Collection<DynamicBeanFactory> beansOfType = container
						.getBeansOfType(DynamicBeanFactory.class);

				for (DynamicBeanFactory dynamicBeanFactory : beansOfType) {
					if (!dynamicBeanFactory.getClass().equals(this.getClass())) {
						if (dynamicBeanFactory.getObjectType().equals(
								this.getObjectType())) {

							return (T) dynamicBeanFactory.getInstance();
						}
					}
				}
			} catch (NoSuchBeanDefinitionException e1) {
				//
			}
		}
		return this.getInstanceInternal();
	}

	protected abstract T getInstanceInternal()
			throws NoSuchBeanDefinitionException;

	public ComponentFactoryContainer getContainer() {
		return container;
	}
}
