package br.com.cd.scaleframework.beans.factory.ioc;

import java.util.Collection;

import javax.servlet.ServletContext;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public interface ComponentFactoryContainer {

	public static final String PACKAGES_TO_SCAN_PARAM_NAME = "packagesToScan";

	void start();

	void stop();

	boolean containsBean(String beanName);

	<T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException;

	Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException;

	<T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException;

	<T> Collection<T> getBeansOfType(Class<T> beanType)
			throws NoSuchBeanDefinitionException;

	DynamicBeanDiscoveryFactory getDiscoveryFactory()
			throws ConfigurationException;

	ServletContext getServletContext();

	Class<?> createComponentProxy(DynamicBean<?> beanConfig)
			throws NoSuchBeanDefinitionException;

	Object getComponent(DynamicBean<?> beanConfig)
			throws NoSuchBeanDefinitionException;

	String generateBeanName(DynamicBean<?> beanConfig);

}