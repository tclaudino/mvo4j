package br.com.cd.mvo.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.util.proxy.MethodHandler;
import br.com.cd.mvo.ioc.MethodInvokeCallback.InvokeCallback;
import br.com.cd.util.javassist.JavassistUtils;

public class JavassistProxifier extends AbstractProxifier {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxify(final String classNameSuffix, final Class<T> targetBeanClass, final Object targetBean, Constructor<?> ctor,
			final MethodInvokeCallback miCallback, Object... parameters) throws ConfigurationException {

		try {

			Class<T> proxyClass = (Class<T>) JavassistUtils.createProxyClass(classNameSuffix, targetBeanClass, targetBean.getClass());

			MethodHandler handler = new MethodHandler() {

				@Override
				public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

					InvokeCallback invokeCallback = new InvokeCallback() {

						@Override
						public Object invoke() throws Throwable {
							Object result = thisMethod.invoke(targetBean, args);

							if (miCallback != null)
								miCallback
										.afterInvoke(self,
												targetBean.getClass().getMethod(thisMethod.getName(), thisMethod.getParameterTypes()),
												result, args);

							return result;
						}

						@Override
						public Object invokeSuper() throws Throwable {
							Object result = proceed.invoke(self, args);

							if (miCallback != null)
								miCallback
										.afterInvoke(self,
												targetBean.getClass().getMethod(thisMethod.getName(), thisMethod.getParameterTypes()),
												result, args);

							return result;
						}
					};

					if ((miCallback == null || miCallback.isCandidateMethod(thisMethod))
							&& containsMethod(targetBean.getClass().getMethods(), thisMethod)) {

						if (miCallback != null) {
							return miCallback.beforeInvoke(self, thisMethod, invokeCallback, args);
						}
						return invokeCallback.invoke();
					}

					return invokeCallback.invokeSuper();
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
