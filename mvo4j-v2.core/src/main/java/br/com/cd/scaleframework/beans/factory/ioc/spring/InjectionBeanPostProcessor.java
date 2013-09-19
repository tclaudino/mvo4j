package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

public class InjectionBeanPostProcessor extends
		AutowiredAnnotationBeanPostProcessor {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Constructor[] determineCandidateConstructors(Class beanClass,
			String beanName) throws BeansException {
		Constructor[] candidates = super.determineCandidateConstructors(
				beanClass, beanName);
		if (candidates == null) {
			Constructor constructor = checkIfThereIsOnlyOneNonDefaultConstructor(beanClass);
			if (constructor != null) {
				candidates = new Constructor[] { constructor };
			}
		}
		return candidates;
	}

	@SuppressWarnings({ "rawtypes" })
	private Constructor checkIfThereIsOnlyOneNonDefaultConstructor(
			Class beanClass) {
		Constructor[] constructors = beanClass.getDeclaredConstructors();
		if (constructors.length == 1) {
			if (constructors[0].getParameterTypes().length > 0) {
				return constructors[0];
			}
		}
		return null;
	}
}
