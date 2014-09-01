package br.com.cd.mvo.ioc;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.ioc.scan.AnnotatedComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.EntityComponentScanner;
import br.com.cd.mvo.ioc.scan.SubTypesComponentScanner;
import br.com.cd.mvo.orm.AbstractRepositoryFactory;
import br.com.cd.mvo.orm.RepositoryFactory;

public abstract class AbstractContainer implements Container {

	protected static Logger looger = Logger.getLogger(Container.class);

	protected ContainerConfig<?> containerConfig;

	protected Collection<ComponentFactory<BeanFactory<?, ?>>> componentFactories = new TreeSet<>();

	public AbstractContainer(ContainerConfig<?> containerConfig) {

		this.containerConfig = containerConfig;
	}

	protected abstract void doRegisterBean(String beanName, String scope, Class<?> beanType);

	protected abstract <T> Object newSingletonFactoryBean(ComponentFactory<T> cf);

	protected abstract void doConfigure();

	protected abstract void doDeepRegister();

	@Override
	public ContainerConfig<?> getContainerConfig() {
		return containerConfig;
	}

	@Override
	public ComponentScannerFactory getComponentScannerFactory() throws ConfigurationException {
		ComponentScannerFactory scannerFactory;
		try {
			scannerFactory = this.getBean(ComponentScannerFactory.BEAN_NAME, ComponentScannerFactory.class);
		} catch (NoSuchBeanDefinitionException e) {

			String packagesToScan = this.getContainerConfig().getInitParameter(ConfigParamKeys.PACKAGES_TO_SCAN);
			if (packagesToScan.isEmpty()) {
				throw new ConfigurationException("No parameter '" + ConfigParamKeys.PACKAGES_TO_SCAN + "' configured");
			}

			scannerFactory = new ComponentScannerFactory(packagesToScan.split(","));
			scannerFactory.addComponentScanner(new AnnotatedComponentScanner(packagesToScan.split(",")));
			scannerFactory.addComponentScanner(new EntityComponentScanner(packagesToScan.split(",")));
			scannerFactory.addComponentScanner(new SubTypesComponentScanner(packagesToScan.split(",")));

			this.registerSingleton(ComponentScannerFactory.BEAN_NAME, scannerFactory);
		}

		return scannerFactory;
	}

	@Override
	public void addComponentFactory(ComponentFactory<BeanFactory<?, ?>> componentFactoy) {
		this.componentFactories.add(componentFactoy);
	}

	@Override
	public <F extends BeanFactory<?, ?>> void addBeanFactory(F beanFactoy) {

		this.addComponentFactory(new BeanFactoryComponentFactory<BeanFactory<?, ?>>(this, beanFactoy));
	}

	@Override
	public Collection<ComponentFactory<BeanFactory<?, ?>>> getComponentFactories() {
		return Collections.unmodifiableCollection(componentFactories);
	}

	@Override
	public <T> void registerBean(ComponentFactory<T> cf) {

		this.registerSingleton(cf.getComponentType().getName(), this.newSingletonFactoryBean(cf));
	}

	@Override
	public void registerBean(BeanMetaDataWrapper<?> metaDataWrapper) throws ConfigurationException, NoSuchBeanDefinitionException {

		for (ComponentFactory<BeanFactory<?, ?>> cf : componentFactories) {

			@SuppressWarnings("rawtypes")
			BeanFactory bf = cf.getInstance();
			if (bf.isCandidate(metaDataWrapper.getBeanMetaData())) {

				try {
					this.doRegisterBean(metaDataWrapper.getBeanMetaData().name(), metaDataWrapper.getBeanMetaData().scope(), metaDataWrapper.getTargetBean());

					this.registerAlias(metaDataWrapper.getBeanMetaData().name(), metaDataWrapper.getBeanMetaData().path());

					this.registerSingleton(metaDataWrapper.getTargetBean().getName(), metaDataWrapper);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanFactoryName(metaDataWrapper), bf);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper, false), metaDataWrapper);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper.getBeanMetaData().getClass(), metaDataWrapper
							.getBeanMetaData().targetEntity()), metaDataWrapper);

					this.registerSingleton(
							BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper.getTargetBean(), metaDataWrapper.getBeanMetaData().targetEntity()),
							metaDataWrapper);

				} catch (Exception e) {

					return;
				}

				return;
			}
		}
		// TODO: LOG
		System.out.println("ComponentFactory not found for " + metaDataWrapper.getTargetBean() + " with targetEntity "
				+ metaDataWrapper.getBeanMetaData().targetEntity());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <P extends RepositoryFactory> P getPersistenceManagerFactory(String beanName, Class<P> impl) {

		P bean;
		try {
			bean = this.getBean(beanName, impl);
		} catch (NoSuchBeanDefinitionException e) {
			this.registerBean(beanName, impl);
			bean = this.getBean(beanName, impl);
		}
		return bean;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RepositoryFactory getPersistenceManagerFactory() throws ConfigurationException {

		String className = this.getContainerConfig().getInitParameter(ConfigParamKeys.REPOSITORY_FACTORY_CLASS,
				ConfigParamKeys.DefaultValues.REPOSITORY_FACTORY_CLASS);

		Class<? extends RepositoryFactory> type;
		try {
			type = (Class<? extends RepositoryFactory>) Class.forName(className);
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new ConfigurationException(e);
		}

		String beanName = AbstractRepositoryFactory.getBeanName(this.getContainerConfig().getInitParameter(
				ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME, ConfigParamKeys.DefaultValues.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME));

		return this.getPersistenceManagerFactory(beanName, type);
	}

	@Override
	public void configure() {

		this.getContainerConfig().getListener().configure(this);
		this.doConfigure();
	}

	@Override
	public void deepRegister() {

		this.getContainerConfig().getListener().deepRegister(this);
		this.doDeepRegister();
	}

}