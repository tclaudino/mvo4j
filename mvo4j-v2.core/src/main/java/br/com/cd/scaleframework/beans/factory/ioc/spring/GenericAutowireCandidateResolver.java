package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class GenericAutowireCandidateResolver extends
		SimpleAutowireCandidateResolver {

	private ComponentFactoryContainer container;

	public GenericAutowireCandidateResolver(ComponentFactoryContainer container) {
		this.container = container;
	}

	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		ParameterizedType pt = (ParameterizedType) descriptor.getField()
				.getGenericType();

		if (pt != null) {
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length >= 1) {

				final Type type = actualTypeArguments[0];

				String beanName = SpringBeanRegistry.resolveBeanConfigName(
						descriptor.getField().getType(), type.getClass());

				DynamicBean<?> beanConfig;
				try {
					beanConfig = container.getBean(beanName, DynamicBean.class);

					return container.getComponent(beanConfig);
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
