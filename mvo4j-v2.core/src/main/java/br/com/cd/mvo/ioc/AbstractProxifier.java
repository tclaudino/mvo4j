package br.com.cd.mvo.ioc.support;

import java.lang.reflect.Constructor;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.ioc.Proxifier;

public abstract class AbstractProxifier implements Proxifier {

	@Override
	public <T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, Constructor<?> ctor, Object... parameters)
			throws ConfigurationException {

		return this.proxify(classNameSuffix, targetBean, bean, ctor, null, parameters);
	}

	@Override
	public <T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, MethodInvokeCallback miCallback, Object... parameters)
			throws ConfigurationException {

		return this.proxify(classNameSuffix, targetBean, bean, null, miCallback, parameters);
	}

	@Override
	public <T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, Object... parameters) throws ConfigurationException {

		return this.proxify(classNameSuffix, targetBean, bean, null, null, parameters);
	}

}
