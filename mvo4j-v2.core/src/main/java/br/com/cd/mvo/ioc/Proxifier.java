package br.com.cd.mvo.ioc;

import java.lang.reflect.Constructor;

import br.com.cd.mvo.core.ConfigurationException;

public interface Proxifier {

	public String BEAN_NAME = Proxifier.class.getName();

	<T> T proxify(final String classNameSuffix, final Class<T> targetBean, final Object bean, Constructor<?> ctor, Object... parameters)
			throws ConfigurationException;

	<T> T proxify(final String classNameSuffix, final Class<T> targetBean, final Object bean, Constructor<?> ctor,
			MethodInvokeCallback miCallback, Object... parameters) throws ConfigurationException;

	<T> Class<T> proxify(String classNameSuffix, final Class<T> targetBean) throws ConfigurationException;

	<T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, MethodInvokeCallback miCallback, Object... parameters)
			throws ConfigurationException;

	<T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, Object... parameters) throws ConfigurationException;

}