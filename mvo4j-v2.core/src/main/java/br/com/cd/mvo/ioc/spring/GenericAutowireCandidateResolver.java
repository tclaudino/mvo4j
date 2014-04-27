package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.util.StringUtils;
import br.com.cd.util.ThreadLocalMapUtil;

public class GenericAutowireCandidateResolver implements AutowireCandidateResolver {

	static Logger looger = Logger.getLogger(GenericAutowireCandidateResolver.class);

	private Container container;
	private AutowireCandidateResolver delegate;

	public GenericAutowireCandidateResolver(Container container, AutowireCandidateResolver delegate) {
		this.container = container;
		this.delegate = delegate;
	}

	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		if (descriptor.getMethodParameter() == null)
			return delegate.getSuggestedValue(descriptor);

		looger.debug("...............................................................................");
		Type genericParameterType = descriptor.getMethodParameter().getGenericParameterType();

		BeanMetaDataWrapper<?> metaData;
		if (genericParameterType != null && (genericParameterType instanceof ParameterizedType)) {

			looger.debug(StringUtils.format("lookup generic depedency '{0}' for '{1}'", genericParameterType, descriptor
					.getMethodParameter().getDeclaringClass()));
			metaData = getGeneric((ParameterizedType) genericParameterType, descriptor);
		} else {
			looger.debug(StringUtils.format("lookup depedency '{0}' for '{1}'", descriptor.getMethodParameter().getParameterType(),
					descriptor.getMethodParameter().getDeclaringClass()));
			metaData = get(descriptor.getMethodParameter().getParameterType(), descriptor);
		}

		Object bean = null;
		if (metaData != null) {
			try {
				looger.debug(StringUtils.format("found metaData '{0}'", metaData));
				bean = get(descriptor, metaData);
			} catch (ConfigurationException e) {
				looger.error("error on create bean from metaData, skipping... ", e);
			}
		} else
			looger.debug("not found! skipping...");

		looger.debug("...............................................................................");
		return bean != null ? bean : delegate.getSuggestedValue(descriptor);
	}

	private BeanMetaDataWrapper<?> get(Class<?> parameterType, DependencyDescriptor descriptor) {

		looger.debug("lookup for metaData...");
		BeanMetaDataWrapper<?> metaData = BeanMetaDataWrapper.getBeanMetaData(container, parameterType, descriptor.getMethodParameter()
				.getDeclaringClass());

		boolean isMetaData = BeanMetaData.class.isAssignableFrom(descriptor.getMethodParameter().getParameterType());

		if (metaData == null && isMetaData) {
			String beanName = descriptor.getMethodParameter().getDeclaringClass().getName();
			looger.debug(StringUtils.format("depency's type is metadata. attempt lookup metaData from beanName '{0}'", beanName));

			if (metaData == null && container.containsBean(beanName))
				metaData = container.getBean(beanName, BeanMetaDataWrapper.class);
		}

		return metaData;
	}

	private BeanMetaDataWrapper<?> getGeneric(ParameterizedType parameterType, DependencyDescriptor descriptor) {

		Type[] typeArguments = parameterType.getActualTypeArguments();
		looger.debug(StringUtils.format("is generics '{0}'", (typeArguments != null && typeArguments.length > 0)));
		if (typeArguments != null && typeArguments.length > 0) {

			final Type type = typeArguments[0];

			Class<?> targetEntity = type.getClass();
			if (type instanceof Class)
				targetEntity = (Class<?>) type;

			looger.debug(StringUtils.format("found generic argument '{0}'. lookup for metaData...", targetEntity));

			@SuppressWarnings("rawtypes")
			BeanMetaDataWrapper metaData = BeanMetaDataWrapper.getBeanMetaDataFromTargetEntity(container, descriptor.getMethodParameter()
					.getDeclaringClass(), descriptor.getMethodParameter().getParameterType(), targetEntity);

			looger.debug(StringUtils.format("found metaData '{0}'", metaData));
			return metaData;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object get(DependencyDescriptor descriptor, BeanMetaDataWrapper metaDataWrapper) throws ConfigurationException {

		boolean isMetaData = BeanMetaData.class.isAssignableFrom(descriptor.getMethodParameter().getParameterType());
		looger.debug(StringUtils.format("getting bean from metaData '{0}'. Will be return himself if depency's type is metadata '{1}'",
				metaDataWrapper.getBeanMetaData(), isMetaData));
		if (isMetaData)
			return metaDataWrapper.getBeanMetaData();

		Object threadVariable = ThreadLocalMapUtil.getThreadVariable(BeanMetaDataWrapper.generateBeanName(metaDataWrapper));
		if (threadVariable != null && descriptor.getMethodParameter().getParameterType().isAssignableFrom(threadVariable.getClass())) {
			looger.debug(StringUtils.format("found cached '{0}'. returning...", threadVariable));
			looger.debug("...............................................................................");
			return (BeanObject) threadVariable;
		}

		// injeção de wrapper generico do mesmo tipo no construtor
		boolean isSefBean = descriptor.getMethodParameter().getDeclaringClass().equals(metaDataWrapper.getTargetBean());
		if (isSefBean) {
			br.com.cd.mvo.ioc.BeanFactory bf = container.getBean(BeanMetaDataWrapper.generateBeanFactoryName(metaDataWrapper),
					br.com.cd.mvo.ioc.BeanFactory.class);

			looger.debug(StringUtils.format("this dependence is the same type of bean. get from beanFactory '{0}'", bf));

			BeanObject<?> instance = bf.getInstance(metaDataWrapper.getBeanMetaData());

			bf.postConstruct(instance, metaDataWrapper);

			// to prevent circular bean creation in autowire dependency
			// resolver.
			ThreadLocalMapUtil.setThreadVariable(BeanMetaDataWrapper.generateBeanName(metaDataWrapper), instance);

			looger.debug(StringUtils.format("returning found instance '{0}'", instance));
			return instance;
		}

		looger.debug(StringUtils.format("no cache! lookup by name '{0}'", BeanMetaDataWrapper.generateBeanName(metaDataWrapper)));

		Object bean = container.getBean(BeanMetaDataWrapper.generateBeanName(metaDataWrapper));
		return bean;
	}

	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {

		return delegate.isAutowireCandidate(bdHolder, descriptor);
	}

}
