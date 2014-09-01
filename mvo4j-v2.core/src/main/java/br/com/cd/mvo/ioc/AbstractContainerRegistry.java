package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.ioc.scan.ComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.ReflectionsScanner;
import br.com.cd.mvo.ioc.scan.RepositoryListenerMetaDataFactory;
import br.com.cd.mvo.ioc.scan.Scanner;
import br.com.cd.mvo.ioc.scan.ServiceListenerMetaDataFactory;

public abstract class AbstractContainerRegistry<C extends Container> implements ContainerRegistry<C> {

	protected C container;

	private Scanner scanner;

	public AbstractContainerRegistry(C container) {
		this.container = container;
		this.scanner = new ReflectionsScanner(container);
	}

	protected abstract void configure();

	@Override
	public void register() throws ConfigurationException {

		container.registerSingleton(Container.class.getName(), container);

		this.configure();
		container.configure();

		this.registerLocalComponentes();

		container.deepRegister();
		this.deepRegister();
	}

	@SuppressWarnings("unchecked")
	private void registerLocalComponentes() throws ConfigurationException {

		container.registerBean(new CacheManagerComponentFactory(this.container));
		container.registerBean(new KeyValuesProviderComponentFactory(this.container));
		container.registerBean(new TranslatorComponentFactory(this.container));
		container.registerBean(new ApplicationComponentFactory(this.container));
		container.registerBean(new DataModelComponentFactory(this.container));

		container.addBeanFactory(new RepositoryBeanFactory(this.container));
		container.addBeanFactory(new NullInstanceBeanFactory<>(container, new RepositoryListenerMetaDataFactory()));
		container.addBeanFactory(new ServiceBeanFactory(this.container));
		container.addBeanFactory(new NullInstanceBeanFactory<>(container, new ServiceListenerMetaDataFactory()));

		Class<? extends Proxifier> proxifierType;
		try {
			proxifierType = (Class<? extends Proxifier>) Class.forName(container.getContainerConfig().getInitParameter(
					ConfigParamKeys.PROXIFIER_STRATEGY_CLASS, ConfigParamKeys.DefaultValues.PROXIFIER_STRATEGY_CLASS));
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new ConfigurationException(e);
		}
		container.registerBean(Proxifier.BEAN_NAME, proxifierType);
	}

	private void deepRegister() throws ConfigurationException {

		ComponentScannerFactory scannerFactory = container.getComponentScannerFactory();

		Collection<ComponentScanner> scanners = scannerFactory.getComponentScanners();

		for (ComponentScanner componentScanner : scanners) {

			for (ComponentFactory<BeanFactory<?, ?>> cf : container.getComponentFactories()) {
				componentScanner.addMetaDataFactories(cf.getInstance().getBeanMetaDataFactory());
			}

			componentScanner.scan(scanner, this.container);
		}
	}
}
