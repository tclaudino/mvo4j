package br.com.cd.scaleframework.ioc.spring;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import br.com.cd.scaleframework.ioc.BeanFactoryFacade;
import br.com.cd.scaleframework.ioc.BeanRegistryFacade;

public class SpringContainer implements BeanFactoryFacade, BeanRegistryFacade {

	private final DefaultListableBeanFactory beanFactory;

	public SpringContainer(DefaultListableBeanFactory applicationContext) {
		this.beanFactory = applicationContext;
	}

	@Override
	public BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanFactory;
	}

	@Override
	public void registerBean(Class<?> beanType) {
		AnnotatedBeanDefinitionReader definitionReader = new AnnotatedBeanDefinitionReader(
				(BeanDefinitionRegistry) beanFactory);

		definitionReader.registerBean(beanType);
	}

	@Override
	public void registerBean(String beanName, Class<?> beanType) {
		AnnotatedBeanDefinitionReader definitionReader = new AnnotatedBeanDefinitionReader(
				(BeanDefinitionRegistry) beanFactory);

		definitionReader.registerBean(beanType, beanName);
	}

	@Override
	public void overrideBean(Class<?> beanType, Class<?> overrideBeanType) {
		BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

		AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(
				beanType);

		String beanName = beanNameGenerator.generateBeanName(abd,
				this.beanFactory);
		this.removeBean(beanName);

		this.registerBean(overrideBeanType);
	}

	private void removeBean(String beanName) {

		if (!this.containsBean(beanName)) {
			return;
		}

		this.beanFactory.removeBeanDefinition(beanName);
	}

	@Override
	public Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException {
		return beanFactory.getBean(beanName, args);
	}

	@Override
	public <T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException, ClassCastException {
		return beanFactory.getBean(beanName, beanType, args);
	}

	@Override
	public <T> T getBean(Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException {
		String[] beanNames = beanFactory.getBeanNamesForType(beanType);
		if (beanNames.length > 0) {
			return beanFactory.getBean(beanNames[0], beanType, args);
		} else {
			return beanFactory.getBean(beanType);
		}
	}

	@Override
	public boolean containsBean(String beanName) {
		return beanFactory.containsBean(beanName);
	}

	@Override
	public boolean containsBean(Class<?> beanType) {
		try {
			getBean(beanType);
			return true;
		} catch (NoSuchBeanDefinitionException e) {
			return false;
		}
	}

	@Override
	public boolean containsBean(String beanName, Class<?> beanType) {
		try {
			Object bean = getBean(beanName);
			return (beanType.isAssignableFrom(bean.getClass()));
		} catch (NoSuchBeanDefinitionException e) {
			return false;
		}
	}
}
