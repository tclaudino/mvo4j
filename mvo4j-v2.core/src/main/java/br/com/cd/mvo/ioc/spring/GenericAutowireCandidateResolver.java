package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		ParameterizedType pt = (ParameterizedType) descriptor
				.getMethodParameter().getGenericParameterType();

		if (pt != null) {
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length >= 1) {

				final Type type = actualTypeArguments[0];

				String beanName = container.getBeanMetaDataName(descriptor
						.getMethodParameter().getDeclaringClass(), type
						.getClass());

				BeanMetaDataWrapper beanConfig;
				try {
					beanConfig = container.getBean(beanName,
							BeanMetaDataWrapper.class);

					if (BeanMetaData.class.isAssignableFrom(descriptor
							.getField().getType())) {
						return beanConfig.getBeanMetaData();
					}

					return container.getBean(beanConfig);
				} catch (NoSuchBeanDefinitionException e) {
					//
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
