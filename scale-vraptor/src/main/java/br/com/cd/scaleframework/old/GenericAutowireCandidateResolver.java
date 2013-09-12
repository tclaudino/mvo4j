package br.com.cd.scaleframework.old;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

public class GenericAutowireCandidateResolver extends
		SimpleAutowireCandidateResolver {

	@Override
	public Object getSuggestedValue(DependencyDescriptor descriptor) {

		ParameterizedType pt = (ParameterizedType) descriptor
				.getGenericDependencyType();
		final Type type1 = pt.getActualTypeArguments()[0];
		final Type type2 = pt.getActualTypeArguments()[1];

		return super.getSuggestedValue(descriptor);
	}

	@Override
	public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder,
			DependencyDescriptor descriptor) {

		return super.isAutowireCandidate(bdHolder, descriptor);
	}

}
