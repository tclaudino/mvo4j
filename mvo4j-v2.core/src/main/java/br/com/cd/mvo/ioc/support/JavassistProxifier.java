package br.com.cd.mvo.ioc.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.util.proxy.MethodHandler;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.util.ReflectionUtils;
import br.com.cd.mvo.util.javassist.JavassistUtils;

public class JavassistProxifier extends AbstractProxifier {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxify(final String classNameSuffix, final Class<T> targetBean, final Object bean, Constructor<?> ctor,
			final MethodInvokeCallback miCallback, Object... parameters) throws ConfigurationException {

		try {

			Class<T> proxyClass = (Class<T>) JavassistUtils.createProxyClass(classNameSuffix, targetBean, bean.getClass());

			MethodHandler handler = new MethodHandler() {

				@Override
				public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

					if ((miCallback == null || miCallback.isCandidateMethod(thisMethod))
							&& ReflectionUtils.containsMethod(bean.getClass().getMethods(), thisMethod)) {
						try {
							return thisMethod.invoke(bean, args);
						} catch (Exception e) {
						}
					}
					if (miCallback != null) miCallback.beforeInvoke(proceed);
					Object invoke = proceed.invoke(self, args);
					if (miCallback != null) miCallback.afterInvoke(proceed);
					return invoke;
				}
			};

			T proxy;
			if (parameters.length == 0) {

				proxy = (T) proxyClass.newInstance();
			} else {
				if (ctor == null) {
					Class<?>[] parameterTypes = new Class[parameters.length];

					for (int i = 0; i < parameters.length; i++) {

						parameterTypes[i] = parameters[i].getClass();
						if (parameters[i] instanceof javassist.util.proxy.Proxy)
							parameterTypes[i] = parameters[i].getClass().getSuperclass();
					}
					ctor = proxyClass.getConstructor(parameterTypes);
				}
				proxy = (T) ctor.newInstance(parameters);
			}
			((javassist.util.proxy.Proxy) proxy).setHandler(handler);

			return proxy;

		} catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	public <T> Class<T> proxify(final String classNameSuffix, Class<T> targetBean) throws ConfigurationException {

		try {
			return JavassistUtils.createProxyClass(classNameSuffix, targetBean);
		} catch (NotFoundException | CannotCompileException e) {
			throw new ConfigurationException(e);
		}
	}
}
