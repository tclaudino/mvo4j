package br.com.cd.mvo.ioc.support;

import java.lang.annotation.Annotation;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.util.ProxyUtils;
import br.com.cd.mvo.util.StringUtils;

public abstract class AbstractBeanFactory<D extends BeanMetaData, A extends Annotation>
		implements BeanFactory<D, A> {

	protected final Container container;
	protected final Class<D> metaDataType;
	protected final Class<A> annotationType;

	@SuppressWarnings("unchecked")
	public AbstractBeanFactory(Container container) {
		this.container = container;
		this.metaDataType = GenericsUtils.getTypesFor(this.getClass()).get(0);
		this.annotationType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	protected abstract Class<? extends BeanObject> getBeanType(D metaData);

	@Override
	public boolean isCandidate(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper) {
		return metaDataWrapper.getBeanMetaData().getClass()
				.equals(metaDataType);
	}

	@Override
	public boolean isCandidate(Class<? extends Annotation> annotation) {
		return this.annotationType.equals(annotation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<BeanObject> createProxy(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws NoSuchBeanDefinitionException {

		return (Class<BeanObject>) createProxy(metaDataWrapper.getTargetBean(),
				getBeanType((D) metaDataWrapper.getBeanMetaData()));
	}

	private <S, P> Class<?> createProxy(final Class<S> sourceClass,
			final Class<P> superClass) {

		try {
			Class<P> proxyClass = ProxyUtils.createProxyClass("", sourceClass,
					superClass);

			return proxyClass;
			/*
			 * Enhancer enhancer = new Enhancer();
			 * enhancer.setSuperclass(proxyClass); Set<Class> allInterfaces =
			 * new HashSet<Class>();
			 * allInterfaces.addAll(Arrays.asList(sourceClass.getClass()
			 * .getInterfaces()));
			 * allInterfaces.addAll(Arrays.asList(superClass.getInterfaces()));
			 * enhancer.setInterfaces(allInterfaces .toArray(new
			 * Class[allInterfaces.size()]));
			 * 
			 * enhancer.setCallback(new MethodInterceptor() {
			 * 
			 * public Object intercept(Object proxy, Method method, Object[]
			 * args, MethodProxy methodProxy) throws Throwable {
			 * 
			 * try {
			 * 
			 * S source = AbstractComponentFactory.this.container
			 * .getBean(sourceClass);
			 * 
			 * if ("getSource".equals(method.getName()) &&
			 * superClass.equals(method.getDeclaringClass())) { return source; }
			 * 
			 * if (Arrays.asList(superClass.getDeclaredMethods())
			 * .contains(method)) { return methodProxy.invokeSuper(proxy, args);
			 * } } catch (NoSuchBeanDefinitionException e) { throw new
			 * ConfigurationException(e); }
			 * 
			 * return methodProxy.invoke(source, args); } });
			 * 
			 * return enhancer.createClass();
			 */

		} catch (NotFoundException | CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final String generateBeanName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper) {

		String beanName = metaDataWrapper.getBeanMetaData().name();

		return (beanName != null && !beanName.isEmpty()) ? beanName
				: (StringUtils.cammelCase(metaDataWrapper.getBeanMetaData()
						.targetEntity().getSimpleName()) + metaDataWrapper
						.getBeanMetaData().getBeanNameSuffix());
	}

	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws ConfigurationException {

		throw new ConfigurationException(new UnsupportedOperationException(
				"@TODO: message"));
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public Class<D> getBeanMetaDataType() {
		return metaDataType;
	}

}