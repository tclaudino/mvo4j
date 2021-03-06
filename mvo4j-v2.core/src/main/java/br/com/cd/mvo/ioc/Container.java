package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.orm.RepositoryFactory;

public interface Container {

	void start() throws ConfigurationException;

	void stop();

	boolean containsBean(String beanName);

	<T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException;

	Object getBean(String beanName) throws NoSuchBeanDefinitionException;

	<T> T getBean(String beanName, Class<T> beanType) throws NoSuchBeanDefinitionException;

	<T> Collection<T> getBeansOfType(Class<T> beanType) throws NoSuchBeanDefinitionException;

	void registerBean(String beanName, Class<?> beanType);

	void registerAlias(String beanName, String alias);

	void registerBean(BeanMetaDataWrapper<?> beanManager) throws ConfigurationException, NoSuchBeanDefinitionException;

	void registerSingleton(String beanName, Object singletonObject);

	void addComponentFactory(ComponentFactory<BeanFactory<?, ?>> componentFactory);

	<F extends BeanFactory<?, ?>> void addBeanFactory(F beanFactoy);

	<T> void registerBean(ComponentFactory<T> cf);

	Collection<ComponentFactory<BeanFactory<?, ?>>> getComponentFactories();

	void configure();

	void deepRegister() throws ConfigurationException;

	ContainerConfig<?> getContainerConfig();

	ComponentScannerFactory getComponentScannerFactory() throws ConfigurationException;

	@SuppressWarnings("rawtypes")
	RepositoryFactory getPersistenceManagerFactory() throws ConfigurationException;

	@SuppressWarnings("rawtypes")
	<P extends RepositoryFactory> P getPersistenceManagerFactory(String beanName, Class<P> impl);

}