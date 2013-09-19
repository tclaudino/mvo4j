package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class GenericAutowireCandidateResolver extends
		SimpleAutowireCandidateResolver {

	private ComponentFactoryContainer container;

	public GenericAutowireCandidateResolver(ComponentFactoryContainer container) {
		this.container = container;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		ParameterizedType pt = (ParameterizedType) descriptor.getField()
				.getGenericType();

		if (pt != null) {
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length >= 1) {

				final Type type = actualTypeArguments[0];

				String beanName = container.generateBeanConfigName(descriptor
						.getField().getType(), type.getClass());

				DynamicBeanManager beanConfig;
				try {
					beanConfig = container.getBean(beanName,
							DynamicBeanManager.class);

					if (BeanConfig.class.isAssignableFrom(descriptor.getField()
							.getType())) {
						return beanConfig.getBeanConfig();
					}

					return container.getDynamicBean(beanConfig);
				} catch (NoSuchBeanDefinitionException e) {
					//
				}
			}
		}
		return super.getSuggestedValue(descriptor);
	}

	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder,
			DependencyDescriptor descriptor) {

		return super.isAutowireCandidate(bdHolder, descriptor);
	}

}
