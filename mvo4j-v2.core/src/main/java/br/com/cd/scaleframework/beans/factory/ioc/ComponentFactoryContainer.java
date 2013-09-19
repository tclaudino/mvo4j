package br.com.cd.scaleframework.beans.factory.ioc;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.ServletContext;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.DynamicBean;
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

	<T> Class<T> createComponentProxy(
			DynamicBeanManager<? extends BeanConfig<T, ?>> beanConfig)
			throws NoSuchBeanDefinitionException;

	<T, ID extends Serializable> DynamicBean<T, ID> getDynamicBean(
			DynamicBeanManager<? extends BeanConfig<T, ID>> beanConfig)
			throws NoSuchBeanDefinitionException;

	<T, ID extends Serializable, Config extends BeanConfig<T, ID>> DynamicBean<T, ID> getDynamicBean(
			Class<T> targetEntity, Class<ID> entityIdType,
			Class<Config> beanConfigType) throws NoSuchBeanDefinitionException;

	String generateBeanName(DynamicBeanManager<?> beanConfig);

	String generateBeanConfigName(Class<?> targetBean, Class<?> targetEntity);

	void registerBean(DynamicBeanManager beanManager)
			throws NoSuchBeanDefinitionException;

	void registerSingleton(String beanName, Object singletonObject);

	void registerSingleton(DynamicBeanManager beanManager);

	void deepRegister();

}