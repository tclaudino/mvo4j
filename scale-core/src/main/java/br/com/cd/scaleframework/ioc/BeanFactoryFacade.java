package br.com.cd.scaleframework.ioc;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public interface BeanFactoryFacade {

	Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException;

	<T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException, ClassCastException;

	<T> T getBean(Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException;

	boolean containsBean(String beanName);

	boolean containsBean(Class<?> beanType);

	boolean containsBean(String beanName, Class<?> beanType);
}