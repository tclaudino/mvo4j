package br.com.cd.mvo.ioc;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import br.com.cd.mvo.ApplicationConfig;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.support.BeanFactoryComponentFactory;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.orm.support.AbstractRepositoryFactory;

public abstract class AbstractContainer implements Container {

	protected static Logger looger = Logger.getLogger(Container.class);

	protected ContainerConfig<?> containerConfig;

	protected ApplicationConfig applicationConfig;

	protected Collection<ComponentFactory<BeanFactory<?, ?>>> componentFactories = new TreeSet<>();

	public AbstractContainer(ContainerConfig<?> containerConfig) {

		this.containerConfig = containerConfig;
	}

	protected abstract void doRegisterBean(String beanName, String scope, Class<?> beanType);

	protected abstract <T> Object newSingletonFactoryBean(ComponentFactory<T> cf);

	protected abstract void doConfigure();

	protected abstract void doDeepRegister();

	@Override
	public ApplicationConfig getApplicationConfig() {

		if (applicationConfig == null) {
			synchronized (this) {
				if (applicationConfig == null) {
					try {
						try {
							applicationConfig = this.getBean(ApplicationConfig.BEAN_NAME, ApplicationConfig.class);

							applicationConfig.merge(this.containerConfig);
						} catch (NoSuchBeanDefinitionException e) {

							applicationConfig = ApplicationConfig.get(this.containerConfig);

							this.registerSingleton(ApplicationConfig.BEAN_NAME, applicationConfig);
						}
					} catch (ConfigurationException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return applicationConfig;
	}

	@Override
	public ContainerConfig<?> getContainerConfig() {
		return containerConfig;
	}

	@Override
	public ComponentScannerFactory getComponentScannerFactory() throws ConfigurationException {
		return getApplicationConfig().getComponentScannerFactory(this);
	}

	@Override
	public void addComponentFactory(ComponentFactory<BeanFactory<?, ?>> componentFactoy) {
		this.componentFactories.add(componentFactoy);
	}

	@Override
	public <F extends BeanFactory<?, ?>> void addComponentFactory(F beanFactoy) {

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
					this.doRegisterBean(metaDataWrapper.getBeanMetaData().name(), metaDataWrapper.getBeanMetaData().scope(),
							metaDataWrapper.getTargetBean());

					this.registerSingleton(metaDataWrapper.getTargetBean().getName(), metaDataWrapper);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanFactoryName(metaDataWrapper), bf);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper, false), metaDataWrapper);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper.getBeanMetaData().getClass(),
							metaDataWrapper.getBeanMetaData().targetEntity()), metaDataWrapper);

					this.registerSingleton(BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper.getTargetBean(), metaDataWrapper
							.getBeanMetaData().targetEntity()), metaDataWrapper);

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
	@SuppressWarnings({ "rawtypes" })
	public RepositoryFactory getPersistenceManagerFactory() throws ConfigurationException {

		Class<? extends RepositoryFactory> type = this.getApplicationConfig().getRepositoryFactoryClass();

		String beanName = AbstractRepositoryFactory.getBeanName(getApplicationConfig().getPersistenceManagerFactoryBeanName());

		return this.getPersistenceManagerFactory(beanName, type);
	}

	@Override
	public void configure() {

		this.getContainerConfig().getContainerListener().configure(this);
		this.doConfigure();
	}

	@Override
	public void deepRegister() {

		this.getContainerConfig().getContainerListener().deepRegister(this);
		this.doDeepRegister();
	}

}