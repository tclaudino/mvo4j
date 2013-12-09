package br.com.cd.mvo.ioc.support;

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

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.util.ReflectionUtils;
import br.com.cd.mvo.util.cglib.CglibUtils;

public class CglibProxifier extends AbstractProxifier {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxify(final String classNameSuffix, final Class<T> targetBean, final Object bean, Constructor<?> ctor,
			final MethodInvokeCallback miCallback, Object... parameters) throws ConfigurationException {

		Set<Class<?>> allInterfaces = new HashSet<>();

		allInterfaces.addAll(Arrays.asList(targetBean.getInterfaces()));
		allInterfaces.addAll(ClassUtils.getAllInterfaces(bean.getClass()));

		Class<?> classToProxy = targetBean;
		if (Factory.class.isAssignableFrom(targetBean)) {
			classToProxy = targetBean.getSuperclass();
			// allInterfaces.remove(Factory.class);
		}

		MethodInterceptor interceptor = new MethodInterceptor() {

			@Override
			public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

				if ((miCallback == null || miCallback.isCandidateMethod(method))
						&& ReflectionUtils.containsMethod(bean.getClass().getMethods(), method)) {

					if (miCallback != null && miCallback.beforeInvoke(method)) {
						Object invoke = method.invoke(bean, args);
						if (miCallback != null) miCallback.afterInvoke(method);
						return invoke;
					}
				}
				if (miCallback != null) miCallback.beforeInvoke(method);
				Object invoke = methodProxy.invokeSuper(object, args);
				if (miCallback != null) miCallback.afterInvoke(method);
				return invoke;
			}
		};

		Enhancer enhancer = CglibUtils.createEnhancer(classNameSuffix, classToProxy, interceptor,
				allInterfaces.toArray(new Class[allInterfaces.size()]));

		Object proxyObj;
		if (parameters.length == 0) {
			proxyObj = enhancer.create();
		} else {

			Class<?>[] parameterTypes;
			if (ctor == null) {
				parameterTypes = new Class[parameters.length];

				for (int i = 0; i < parameters.length; i++) {

					parameterTypes[i] = parameters[i].getClass();
					if (parameters[i] instanceof net.sf.cglib.proxy.Factory) parameterTypes[i] = parameters[i].getClass().getSuperclass();
				}
			} else {
				parameterTypes = ctor.getParameterTypes();
			}
			proxyObj = enhancer.create(parameterTypes, parameters);
		}
		return (T) proxyObj;
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

		classToProxy = enhancer.createClass();

		return (Class<T>) classToProxy;
	}
}
