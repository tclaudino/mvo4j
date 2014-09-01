package br.com.cd.mvo.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.apache.commons.lang3.ClassUtils;

import br.com.cd.mvo.ioc.MethodInvokeCallback.InvokeCallback;
import br.com.cd.util.cglib.CglibUtils;

public class CglibProxifier extends AbstractProxifier {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxify(final String classNameSuffix, final Class<T> targetBeanClass, final Object targetBean, Constructor<?> ctor,
			final MethodInvokeCallback miCallback, Object... parameters) throws ConfigurationException {

		Enhancer enhancer = createEnhancer(classNameSuffix, targetBeanClass, targetBean, miCallback);

		Object proxyObj;
		if (parameters.length > 0 || ctor != null) {

			Class<?>[] parameterTypes;

			if (ctor != null) {
				parameterTypes = ctor.getParameterTypes();
			} else {
				parameterTypes = new Class[parameters.length];

				for (int i = 0; i < parameters.length; i++) {

					parameterTypes[i] = parameters[i].getClass();
					if (parameters[i] instanceof net.sf.cglib.proxy.Factory)
						parameterTypes[i] = parameters[i].getClass().getSuperclass();
				}
			}
			proxyObj = enhancer.create(parameterTypes, parameters);

		} else {
			proxyObj = enhancer.create();

		}
		return (T) proxyObj;
	}

	private <T> Enhancer createEnhancer(final String classNameSuffix, final Class<T> targetBeanClass, final Object targetBean,
			final MethodInvokeCallback miCallback) {
		Set<Class<?>> allInterfaces = new HashSet<>();

		allInterfaces.addAll(Arrays.asList(targetBeanClass.getInterfaces()));
		allInterfaces.addAll(ClassUtils.getAllInterfaces(targetBean.getClass()));

		Class<?> classToProxy = targetBeanClass;
		if (Factory.class.isAssignableFrom(targetBeanClass)) {
			classToProxy = targetBeanClass.getSuperclass();
			// allInterfaces.remove(Factory.class);
		}

		MethodInterceptor interceptor = new MethodInterceptor() {

			@Override
			public Object intercept(Object self, Method thisMethod, Object[] args, MethodProxy methodProxy) throws Throwable {

				InvokeCallback invokeCallback = new InvokeCallback() {

					@Override
					public Object invoke() throws Throwable {
						Object result = thisMethod.invoke(targetBean, args);

						if (miCallback != null)
							miCallback.afterInvoke(self,
									targetBean.getClass().getMethod(thisMethod.getName(), thisMethod.getParameterTypes()), result, args);

						return result;
					}

					@Override
					public Object invokeSuper() throws Throwable {
						Object result = methodProxy.invokeSuper(self, args);

						if (miCallback != null)
							miCallback.afterInvoke(self, thisMethod, result, args);

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

		Enhancer enhancer = CglibUtils.createEnhancer(classNameSuffix, classToProxy, interceptor,
				allInterfaces.toArray(new Class[allInterfaces.size()]));
		return enhancer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> proxify(final String classNameSuffix, Class<T> targetBean) throws ConfigurationException {

		Class<?> classToProxy = targetBean;
		if (Factory.class.isAssignableFrom(targetBean)) {
			classToProxy = targetBean.getSuperclass();
		}

		Enhancer enhancer = CglibUtils.createEnhancer(classNameSuffix, classToProxy, null);

		enhancer.setCallbackType(NoOp.class);

		return (Class<T>) enhancer.createClass();
	}
}
