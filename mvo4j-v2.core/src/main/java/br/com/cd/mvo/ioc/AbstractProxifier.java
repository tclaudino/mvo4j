package br.com.cd.mvo.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.com.cd.util.ReflectionUtils;

public abstract class AbstractProxifier implements Proxifier {

	private static Map<Method, Boolean> cachedMethods = new HashMap<>();

	boolean containsMethod(Method[] methods, Method method) {

		Boolean result = cachedMethods.get(method);
		if (result != null)
			return result;

		result = ReflectionUtils.containsMethod(methods, method);
		cachedMethods.put(method, result);

		return result;
	}

	@Override
	public <T> T proxify(String classNameSuffix, Class<T> targetBean, Object bean, Constructor<?> ctor, Object... parameters) throws ConfigurationException {

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
