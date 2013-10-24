package br.com.cd.mvo.ioc.support;

import java.util.Collection;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public abstract class AbstractComponentFactory<T> implements
		ComponentFactory<T> {

	private static int static_order = -1;
	private final int order;

	protected final Container container;
	protected final Class<T> objectType;

	@SuppressWarnings("unchecked")
	public AbstractComponentFactory(Container container) {
		this.container = container;
		this.objectType = GenericsUtils.getTypesFor(this.getClass()).get(0);

		order = static_order++;
	}

	@Override
	public Class<T> getObjectType() {
		return objectType;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public T getInstance() throws NoSuchBeanDefinitionException {

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

		return new Integer(this.getOrder())
				.compareTo(new Integer(o.getOrder()));
	}

	@Override
	public final int getOrder() {
		return order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((container == null) ? 0 : container.hashCode());
		result = prime * result
				+ ((objectType == null) ? 0 : objectType.hashCode());
		result = prime * result + new Integer(order).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractComponentFactory<?> other = (AbstractComponentFactory<?>) obj;
		if (container == null) {
			if (other.container != null)
				return false;
		} else if (!container.equals(other.container))
			return false;
		if (objectType == null) {
			if (other.objectType != null)
				return false;
		} else if (!objectType.equals(other.objectType))
			return false;
		if (order != other.order)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractComponentFactory [order=" + order + ", container="
				+ container + ", objectType=" + objectType + "]";
	}

}
