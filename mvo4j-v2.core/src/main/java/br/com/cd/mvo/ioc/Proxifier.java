package br.com.cd.mvo.ioc;

public interface BeanInstantiationStrategy {

	<T> T instantiate(final Class<T> targetBean, final Object bean,
			Object... arguments);

	<T> Class<T> createProxy(final Class<T> targetBean, final Object instance);

	<T> Class<T> createProxy(final Class<T> targetBean);
}