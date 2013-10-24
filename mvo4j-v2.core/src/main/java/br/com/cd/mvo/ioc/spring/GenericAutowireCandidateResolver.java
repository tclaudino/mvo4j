package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.ComponentFactory;
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

		Type parameterType = descriptor.getMethodParameter()
				.getGenericParameterType();

		if (parameterType == null
				|| !(parameterType instanceof ParameterizedType)) {
			return delegate.getSuggestedValue(descriptor);
		}

		ParameterizedType pt = (ParameterizedType) parameterType;

		if (pt != null) {
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length >= 1) {

				final Type type = actualTypeArguments[0];

				// String beanName = container.getBeanMetaDataName(descriptor
				// .getMethodParameter().getDeclaringClass(), type
				// .getClass());

				Collection<BeanMetaDataWrapper> configs = container
						.getBeansOfType(BeanMetaDataWrapper.class);

				BeanMetaDataWrapper beanConfig = null;
				for (ComponentFactory<BeanFactory<?, ?>> cf : container
						.getComponentFactories()) {

					BeanFactory<?, ?> bf;
					bf = cf.getInstance();
					for (BeanMetaDataWrapper<?> config : configs) {
						if (bf.isCandidate(config)
								&& config.getBeanMetaData().targetEntity()
										.equals(type.getClass())) {

							beanConfig = config;
						}
					}
				}

				if (beanConfig != null) {
					if (BeanMetaData.class.isAssignableFrom(descriptor
							.getField().getType())) {
						return beanConfig.getBeanMetaData();
					}
				} else {
					try {

						return container.getBean(beanConfig);
					} catch (NoSuchBeanDefinitionException e) {
						//
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
