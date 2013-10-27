package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.ConfigurationException;

public interface Proxifier {

	public String BEAN_NAME = Proxifier.class.getName();

	<T> T proxify(final String classNameSuffix, final Class<T> targetBean,
			final Object bean, Object... parameters)
			throws ConfigurationException;

	<T> Class<T> proxify(String classNameSuffix, final Class<T> targetBean)
			throws ConfigurationException;

}