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
import br.com.cd.mvo.orm.PersistenceManagerFactory;
import br.com.cd.mvo.util.StringUtils;

public abstract class AbstractContainer implements Container {

	protected ContainerConfig<?> containerConfig;

	protected Collection<ComponentFactory<BeanFactory<?, ?>>> componentFactories = new TreeSet<>();

	public AbstractContainer(ContainerConfig<?> applicationConfig) {

		this.containerConfig = applicationConfig;
	}

	protected abstract void doRegister(Class<?> type,
			BeanMetaDataWrapper<? extends BeanMetaData> beanConfig);

	protected abstract void doDeepRegister();

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
	public String getBeanName(
			BeanMetaDataWrapper<? extends BeanMetaData> beanConfig) {

		for (ComponentFactory<BeanFactory<?, ?>> dcf : componentFactories) {

			BeanFactory<?, ?> cf;
			cf = dcf.getInstance();
			if (cf.isCandidate(beanConfig)) {
				return cf.generateBeanName(beanConfig);
			}
		}
		return beanConfig.getBeanMetaData().name();
	}

	@Override
	public String getBeanMetaDataName(Class<?> targetBean, Class<?> targetEntity) {

		@SuppressWarnings("rawtypes")
		Collection<BeanMetaDataWrapper> configs = this
				.getBeansOfType(BeanMetaDataWrapper.class);

		for (ComponentFactory<BeanFactory<?, ?>> dcf : componentFactories) {

			BeanFactory<?, ?> cf;
			cf = dcf.getInstance();
			for (BeanMetaDataWrapper<?> config : configs) {
				if (cf.isCandidate(config)
						&& targetBean.isAssignableFrom(config.getTargetBean())
						&& config.getBeanMetaData().targetEntity()
								.equals(targetEntity)) {

					return cf.generateBeanName(config) + "Config";
				}
			}
		}
		return StringUtils.cammelCase(targetBean.getSimpleName()) + "Config";
	}

	@Override
	public BeanObject getBean(
			BeanMetaDataWrapper<? extends BeanMetaData> beanConfig)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory<BeanFactory<?, ?>> dcf : componentFactories) {

			BeanFactory<?, ?> cf = dcf.getInstance();
			if (cf.isCandidate(beanConfig)) {

				String beanName = this.getBeanName(beanConfig);
				return this.getBean(beanName, BeanObject.class);
			}
		}
		throw new NoSuchBeanDefinitionException(beanConfig.getBeanMetaData()
				.toString());
	}

	@Override
	public void registerBean(
			BeanMetaDataWrapper<? extends BeanMetaData> beanManager)
			throws ConfigurationException, NoSuchBeanDefinitionException {

		String beanConfigName = this.getBeanMetaDataName(beanManager
				.getTargetBean(), beanManager.getBeanMetaData().targetEntity());
		this.registerSingleton(beanConfigName, beanManager);

		for (ComponentFactory<BeanFactory<?, ?>> dcf : componentFactories) {

			BeanFactory<?, ?> cf = dcf.getInstance();
			if (cf.isCandidate(beanManager)) {

				if (cf.isSingleton()) {
					this.registerSingleton(
							beanManager.getBeanMetaData().name(),
							cf.getInstance(beanManager));
				} else {
					this.doRegister(cf.createProxy(beanManager), beanManager);
				}
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

	@Override
	public final void deepRegister() {

		this.doDeepRegister();

		containerConfig.getContainerListener().contextLoaded(this);
	}
}