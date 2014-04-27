package br.com.cd.mvo.ioc;

import br.com.cd.util.GenericsUtils;
import br.com.cd.util.ParserUtils;
import br.com.cd.util.ThreadLocalMapUtil;


public abstract class AbstractComponentFactory<T> implements ComponentFactory<T> {

	private static int static_order = -1;
	private final int order;

	protected final Container container;
	protected final Class<T> componentType;

	@SuppressWarnings("unchecked")
	public AbstractComponentFactory(Container container) {
		this.container = container;
		this.componentType = GenericsUtils.getTypesFor(this.getClass()).get(0);

		order = new Integer(++static_order);
	}

	@Override
	public Class<T> getComponentType() {
		return componentType;
	}

	@Override
	public T getInstance() throws NoSuchBeanDefinitionException {

		String threadIdentifier = ParserUtils.parseString(ThreadLocalMapUtil.getThreadVariable(this.getComponentBeanName()));

		try {
			if (threadIdentifier.isEmpty()) {
				ThreadLocalMapUtil.setThreadVariable(this.getComponentBeanName(), this.getComponentBeanName() + "_RUNNING");
				T bean = container.getBean(this.getComponentBeanName(), this.getComponentType());
				ThreadLocalMapUtil.removeThreadVariable(this.getComponentBeanName());

				return bean;
			}
		} catch (NoSuchBeanDefinitionException e) {
		}
		T instance = this.getInstanceInternal();
		if (!container.containsBean(getComponentBeanName())) {
			container.registerSingleton(this.getComponentBeanName(), instance);
		}
		return instance;
	}

	protected abstract String getComponentBeanName();

	protected abstract T getInstanceInternal() throws NoSuchBeanDefinitionException;

	@Override
	public int compareTo(@SuppressWarnings("rawtypes") ComponentFactory o) {

		return new Integer(this.getOrder()).compareTo(new Integer(o.getOrder()));
	}

	@Override
	public final int getOrder() {
		return order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		result = prime * result + ((componentType == null) ? 0 : componentType.hashCode());
		result = prime * result + new Integer(order).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractComponentFactory<?> other = (AbstractComponentFactory<?>) obj;
		if (container == null) {
			if (other.container != null) return false;
		} else if (!container.equals(other.container)) return false;
		if (componentType == null) {
			if (other.componentType != null) return false;
		} else if (!componentType.equals(other.componentType)) return false;
		if (order != other.order) return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractComponentFactory [order=" + order + ", container=" + container + ", objectType=" + componentType + "]";
	}

}
