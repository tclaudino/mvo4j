package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.InitApplicationConfig;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.orm.PersistenceManagerFactory;

public interface Container {

	void start();

	void stop();

	boolean containsBean(String beanName);

	<T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException;

	Object getBean(String beanName) throws NoSuchBeanDefinitionException;

	<T> T getBean(String beanName, Class<T> beanType)
			throws NoSuchBeanDefinitionException;

	<T> Collection<T> getBeansOfType(Class<T> beanType)
			throws NoSuchBeanDefinitionException;

	BeanObject getBean(BeanMetaDataWrapper<? extends BeanMetaData> beanConfig)
			throws NoSuchBeanDefinitionException;

	String getBeanName(BeanMetaDataWrapper<? extends BeanMetaData> beanConfig);

	String getBeanMetaDataName(Class<?> targetBean, Class<?> targetEntity);

	void registerBean(Class<?> beanType, String beanName);

	void registerBean(BeanMetaDataWrapper<? extends BeanMetaData> beanManager)
			throws ConfigurationException, NoSuchBeanDefinitionException;

	void registerSingleton(String beanName, Object singletonObject);

	void addComponentFactory(
			ComponentFactory<BeanFactory<?, ?>> componentFactory);

	Collection<ComponentFactory<BeanFactory<?, ?>>> getComponentFactories();

	void deepRegister();

	ContainerConfig<?> getContainerConfig();

	InitApplicationConfig getInitApplicationConfig();

	ComponentScannerFactory getComponentScannerFactory()
			throws ConfigurationException;

	@SuppressWarnings("rawtypes")
	PersistenceManagerFactory getPersistenceManagerFactory()
			throws ConfigurationException;

	@SuppressWarnings("rawtypes")
	<P extends PersistenceManagerFactory> P getPersistenceManagerFactory(
			String beanName, Class<P> impl);

}