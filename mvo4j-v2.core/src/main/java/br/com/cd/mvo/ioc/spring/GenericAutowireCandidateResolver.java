package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;

public class GenericAutowireCandidateResolver implements
		AutowireCandidateResolver {

	private Container container;
	private AutowireCandidateResolver delegate;

	public GenericAutowireCandidateResolver(Container container,
			AutowireCandidateResolver delegate) {
		this.container = container;
		this.delegate = delegate;
	}

	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		if (descriptor.getMethodParameter() == null)
			return delegate.getSuggestedValue(descriptor);

		Type genericParameterType = descriptor.getMethodParameter()
				.getGenericParameterType();

		if (genericParameterType != null
				&& (genericParameterType instanceof ParameterizedType)) {

			return handleGeneric((ParameterizedType) genericParameterType,
					descriptor);
		}

		return handle(descriptor.getMethodParameter().getParameterType(),
				descriptor);
	}

	private Object handle(Class<?> parameterType,
			DependencyDescriptor descriptor) {

		// String beanName = container.getBeanMetaDataName(descriptor
		// .getMethodParameter().getDeclaringClass(), type
		// .getClass());

		BeanMetaDataWrapper<?> metaData = BeanMetaDataWrapper.getBeanMetaData(
				container, parameterType, descriptor.getMethodParameter()
						.getDeclaringClass());

		if (metaData != null) {
			if (BeanMetaData.class.isAssignableFrom(parameterType)) {
				return metaData.getBeanMetaData();
			} else {
				try {
					return container.getBean(BeanMetaDataWrapper
							.generateBeanName(metaData));
				} catch (NoSuchBeanDefinitionException e) {
					//
				}
			}
		}
		return delegate.getSuggestedValue(descriptor);
	}

	private Object handleGeneric(ParameterizedType parameterType,
			DependencyDescriptor descriptor) {

		if (parameterType != null) {

			Type[] typeArguments = parameterType.getActualTypeArguments();
			if (typeArguments != null && typeArguments.length > 0) {

				final Type type = typeArguments[0];

				// String beanName = container.getBeanMetaDataName(descriptor
				// .getMethodParameter().getDeclaringClass(), type
				// .getClass());

				BeanMetaDataWrapper<?> metaData = BeanMetaDataWrapper
						.getBeanMetaDataFromTargetEntity(container, descriptor
								.getMethodParameter().getParameterType(), type
								.getClass());

				if (metaData != null) {
					if (BeanMetaData.class.isAssignableFrom(descriptor
							.getField().getType())) {
						return metaData.getBeanMetaData();
					} else {
						try {
							return container.getBean(BeanMetaDataWrapper
									.generateBeanName(metaData));
						} catch (NoSuchBeanDefinitionException e) {
							//
						}
					}
				}
			}
		}
		return delegate.getSuggestedValue(descriptor);
	}

	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder,
			DependencyDescriptor descriptor) {

		return delegate.isAutowireCandidate(bdHolder, descriptor);
	}

}
