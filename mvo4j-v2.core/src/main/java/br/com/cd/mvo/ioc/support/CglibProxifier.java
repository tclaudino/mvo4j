package br.com.cd.mvo.ioc.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import br.com.cd.mvo.ioc.Proxifier;
import br.com.cd.mvo.util.ReflectionUtils;
import br.com.cd.mvo.util.ThreadLocalMapUtil;
import br.com.cd.mvo.util.cglib.CglibUtils;

public class CglibProxifier implements Proxifier {

	private static boolean isCandidateMethod(Method method) {

		return Modifier.isPublic(method.getModifiers())
				&& !Modifier.isFinal(method.getModifiers())
				&& !Modifier.isNative(method.getModifiers())
				&& !Modifier.isStatic(method.getModifiers())
				&& !Modifier.isStrict(method.getModifiers())
				&& !Modifier.isVolatile(method.getModifiers())
				&& !ReflectionUtils.getJavaObjectMethods().contains(
						method.getName());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxify(final String classNameSuffix,
			final Class<T> targetBean, final Object bean, Object... parameters) {

		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.addAll(Arrays.asList(targetBean.getInterfaces()));
		allInterfaces.addAll(Arrays.asList(bean.getClass().getInterfaces()));

		Enhancer enhancer = CglibUtils.createEnhancer(classNameSuffix,
				targetBean, new MethodInterceptor() {

					@Override
					public Object intercept(Object object, Method method,
							Object[] args, MethodProxy methodProxy)
							throws Throwable {

						if (isCandidateMethod(method)
								&& ReflectionUtils.containsMethod(bean
										.getClass().getMethods(), method)) {

							Object invoke = method.invoke(bean, args);
							return invoke;
						}
						applySetParameterAnnotations(method);
						Object invoke = methodProxy.invokeSuper(object, args);
						applyUnsetParameterAnnotations(method);
						return invoke;
					}
				}, allInterfaces.toArray(new Class[allInterfaces.size()]));

		if (parameters.length > 0) {

			Class<?>[] parameterTypes = new Class[parameters.length];

			for (int i = 0; i < parameters.length; i++) {

				parameterTypes[i] = parameters[i].getClass();
				if (parameters[i] instanceof net.sf.cglib.proxy.Factory)
					parameterTypes[i] = parameters[i].getClass()
							.getSuperclass();
			}
			return (T) enhancer.create(parameterTypes, parameters);
		}
		return (T) enhancer.create();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> proxify(final String classNameSuffix,
			Class<T> targetBean) {

		Enhancer enhancer = CglibUtils.createEnhancer(classNameSuffix,
				targetBean, null);

		enhancer.setCallbackType(NoOp.class);

		return (Class<T>) enhancer.createClass();
	}

	private void applyUnsetParameterAnnotations(Method method) {

		ThreadLocalMapUtil.removeThreadVariable("PARAMETER_ANNOTATIONS");
	}

	private void applySetParameterAnnotations(Method method) {

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		if (parameterAnnotations.length > 0
				&& parameterAnnotations[0].length > 0)
			ThreadLocalMapUtil.setThreadVariable("PARAMETER_ANNOTATIONS",
					joinAnnotations(method.getParameterAnnotations()));
	}

	private Annotation[] joinAnnotations(Annotation[][] annotationsArray) {

		List<Annotation> list = new LinkedList<>();
		for (Annotation[] annotations : annotationsArray) {
			if (annotations.length > 0)
				list.addAll(Arrays.asList(annotations));
		}
		return list.toArray(new Annotation[list.size()]);
	}
}
