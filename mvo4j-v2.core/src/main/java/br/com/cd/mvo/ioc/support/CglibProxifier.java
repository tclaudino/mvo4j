package br.com.cd.mvo.ioc.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import br.com.cd.mvo.ioc.BeanInstantiationStrategy;

public class CglibBeanInstantiationStrategy implements
		BeanInstantiationStrategy {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T instantiate(final Class<T> targetBean, final Object bean,
			Object... arguments) {

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetBean);
		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.addAll(Arrays.asList(targetBean.getInterfaces()));
		allInterfaces.addAll(Arrays.asList(bean.getClass().getInterfaces()));
		enhancer.setInterfaces(allInterfaces.toArray(new Class[allInterfaces
				.size()]));

		enhancer.setCallback(new MethodInterceptor() {

			@Override
			public Object intercept(Object object, Method method,
					Object[] args, MethodProxy methodProxy) throws Throwable {

				if (!Arrays.asList(bean.getClass().getDeclaredMethods())
						.contains(method)) {
					try {
						return method.invoke(bean, args);
					} catch (Exception e) {
					}
				}
				return methodProxy.invokeSuper(object, args);
			}
		});

		if (arguments.length > 0) {
			Class<?>[] argumentTypes = new Class[arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				argumentTypes[i] = arguments[i].getClass();
			}
			return (T) enhancer.create(argumentTypes, arguments);
		}
		return (T) enhancer.create();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> createProxy(final Class<T> targetBean,
			final Object instance) {

		Set<Class<?>> allInterfaces = new HashSet<>();
		allInterfaces.addAll(Arrays.asList(targetBean.getInterfaces()));
		allInterfaces
				.addAll(Arrays.asList(instance.getClass().getInterfaces()));

		Enhancer enhancer = createEnhancer(targetBean, new MethodInterceptor() {

			@Override
			public Object intercept(Object object, Method method,
					Object[] args, MethodProxy methodProxy) throws Throwable {

				if (!Arrays.asList(object.getClass().getDeclaredMethods())
						.contains(method)) {
					try {
						return method.invoke(instance, args);
					} catch (Exception e) {
					}
				}
				return methodProxy.invokeSuper(object, args);
			}
		}, allInterfaces.toArray(new Class<?>[allInterfaces.size()]));

		return (Class<T>) enhancer.createClass();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> createProxy(Class<T> targetBean) {

		Enhancer enhancer = createEnhancer(targetBean, new MethodInterceptor() {

			@Override
			public Object intercept(Object object, Method method,
					Object[] args, MethodProxy methodProxy) throws Throwable {

				return methodProxy.invokeSuper(object, args);
			}
		});

		return (Class<T>) enhancer.createClass();
	}

	private Enhancer createEnhancer(Class<?> targetBean,
			MethodInterceptor interceptor, Class<?>... allInterfaces) {

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetBean);
		List<Class<?>> interfacesList = Arrays.asList(allInterfaces);
		if (interfacesList.isEmpty())
			interfacesList.addAll(Arrays.asList(targetBean.getInterfaces()));
		enhancer.setInterfaces(interfacesList.toArray(new Class[interfacesList
				.size()]));

		enhancer.setCallback(interceptor);

		return enhancer;
	}

}
