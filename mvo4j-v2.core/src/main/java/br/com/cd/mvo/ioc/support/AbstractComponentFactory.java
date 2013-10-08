package br.com.cd.mvo.ioc.support;

import java.util.Collection;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public abstract class AbstractComponentFactory<T> implements
		ComponentFactory<T> {

	private static int order = -1;

	protected final Container container;
	protected final Class<T> objectType;

	@SuppressWarnings("unchecked")
	public AbstractComponentFactory(Container container) {
		this.container = container;
		this.objectType = GenericsUtils.getTypesFor(this.getClass()).get(0);

		order++;
	}

	@Override
	public Class<T> getObjectType() {
		return objectType;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final T getInstance() throws NoSuchBeanDefinitionException {

		try {
			return container.getBean(this.getObjectType());
		} catch (NoSuchBeanDefinitionException e) {

			try {

				Collection<ComponentFactory> beansOfType = container
						.getBeansOfType(ComponentFactory.class);

				for (ComponentFactory factory : beansOfType) {

					if (!factory.getClass().equals(this.getClass())) {
						if (factory.getObjectType()
								.equals(this.getObjectType())) {

							return (T) factory.getInstance();
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

	@Override
	public int compareTo(@SuppressWarnings("rawtypes") ComponentFactory o) {
		if (this.getOrder() < o.getOrder()) {
			return -1;
		}
		if (this.getOrder() > o.getOrder()) {
			return 1;
		}
		return 0;
	}

	@Override
	public final int getOrder() {
		return order;
	}
}
