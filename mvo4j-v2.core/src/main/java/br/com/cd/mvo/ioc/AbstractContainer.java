package br.com.cd.mvo.ioc;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import br.com.cd.mvo.InitApplicationConfig;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.support.BeanFactoryUtils;
import br.com.cd.mvo.ioc.support.BeanObjectComponentFactory;
import br.com.cd.mvo.orm.PersistenceManagerFactory;

public abstract class AbstractContainer implements Container {

	protected ContainerConfig<?> containerConfig;

	protected Collection<ComponentFactory<BeanFactory<?, ?>>> componentFactories = new TreeSet<>();

	public AbstractContainer(ContainerConfig<?> applicationConfig) {

		this.containerConfig = applicationConfig;
	}

	protected abstract void doRegister(String beanName, String scope,
			Class<?> type);

	@Override
	public void addComponentFactory(
			ComponentFactory<BeanFactory<?, ?>> componentFactoy) {
		this.componentFactories.add(componentFactoy);
	}

	@Override
	public Collection<ComponentFactory<BeanFactory<?, ?>>> getComponentFactories() {
		return Collections.unmodifiableCollection(componentFactories);
	}

	@Override
	public InitApplicationConfig getInitApplicationConfig() {

		InitApplicationConfig initApplicationConfig;
		try {
			try {
				initApplicationConfig = this
						.getBean(InitApplicationConfig.class);

				initApplicationConfig.merge(this.containerConfig);
			} catch (NoSuchBeanDefinitionException e) {

				initApplicationConfig = InitApplicationConfig
						.get(this.containerConfig);

				this.registerSingleton(InitApplicationConfig.class.getName(),
						initApplicationConfig);
			}
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}

		return initApplicationConfig;
	}

	@Override
	public ContainerConfig<?> getContainerConfig() {
		return containerConfig;
	}

	@Override
	public ComponentScannerFactory getComponentScannerFactory()
			throws ConfigurationException {
		return getInitApplicationConfig().getComponentScannerFactory(this);
	}

	@Override
	public BeanObject getBean(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory<BeanFactory<?, ?>> cf : componentFactories) {

			BeanFactory<?, ?> bf = cf.getInstance();
			if (bf.isCandidate(metaDataWrapper)) {

				return this.getBean(
						BeanFactoryUtils.generateBeanName(metaDataWrapper),
						BeanObject.class);
			}
		}
		throw new NoSuchBeanDefinitionException(
				BeanFactoryUtils.generateBeanName(metaDataWrapper));
	}

	@Override
	public void registerBean(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws ConfigurationException, NoSuchBeanDefinitionException {

		for (ComponentFactory<BeanFactory<?, ?>> cf : componentFactories) {

			BeanFactory<?, ?> bf = cf.getInstance();
			if (bf.isCandidate(metaDataWrapper)) {

				if (bf.isSingleton()) {

					BeanObject instance = bf.getInstance(metaDataWrapper);
					String name = metaDataWrapper.getBeanMetaData().name();
					this.registerSingleton(name, instance);

					// String generatedName = BeanFactoryUtils.generateBeanName(
					// metaDataWrapper, true);
					// if (generatedName.equals(name))
					// this.registerSingleton(generatedName, instance);
				} else {

					Class<BeanObject> proxy = bf.createProxy(metaDataWrapper);

					String beanName = metaDataWrapper.getBeanMetaData().name();

					this.doRegister(proxy.getName(), metaDataWrapper
							.getBeanMetaData().scope(), proxy);

					this.registerSingleton(
							beanName,
							this.getSingletonBeanFactory(new BeanObjectComponentFactory<>(
									this, proxy, proxy.getName())));

					// String generatedName = BeanFactoryUtils.generateBeanName(
					// metaDataWrapper, true);
					// if (generatedName.equals(beanName)) {
					//
					// this.doRegister(generatedName, metaDataWrapper
					// .getBeanMetaData().scope(), proxy);
					//
					// this.registerSingleton(
					// beanName,
					// this.getSingletonBeanFactory(new
					// BeanObjectComponentFactory<>(
					// this, proxy, proxy.getName())));
					// }
				}

				this.registerSingleton(BeanFactoryUtils
						.generateBeanMetaDataName(metaDataWrapper),
						metaDataWrapper);

				break;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <P extends PersistenceManagerFactory> P getPersistenceManagerFactory(
			String beanName, Class<P> impl) {

		P bean;
		try {
			bean = getBean(beanName, impl);
		} catch (NoSuchBeanDefinitionException e) {
			this.registerBean(impl, beanName);
			bean = getBean(beanName, impl);
		}
		return bean;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PersistenceManagerFactory getPersistenceManagerFactory()
			throws ConfigurationException {

		String persistenceManagerFactoryClass = this.getInitApplicationConfig()
				.getPersistenceManagerFactoryClass();
		try {
			Class<? extends PersistenceManagerFactory> type = (Class<? extends PersistenceManagerFactory>) Class
					.forName(persistenceManagerFactoryClass);

			String beanName = PersistenceManagerFactory
					.getBeanName(getInitApplicationConfig()
							.getPersistenceFactoryQualifier());

			return this.getPersistenceManagerFactory(beanName, type);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(e);
		}
	}

}