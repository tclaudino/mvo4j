package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.CacheManager;
import br.com.cd.mvo.KeyValuesProvider;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.mvo.ioc.scan.ComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.ReflectionsScanner;
import br.com.cd.mvo.ioc.scan.Scanner;
import br.com.cd.mvo.ioc.support.ApplicationComponentFactory;
import br.com.cd.mvo.ioc.support.BeanFactoryComponentFactory;
import br.com.cd.mvo.ioc.support.CacheManagerComponentFactory;
import br.com.cd.mvo.ioc.support.DataModelComponentFactory;
import br.com.cd.mvo.ioc.support.KeyValuesProviderComponentFactory;
import br.com.cd.mvo.ioc.support.RepositoryBeanFactory;
import br.com.cd.mvo.ioc.support.ServiceBeanFactory;
import br.com.cd.mvo.ioc.support.TranslatorComponentFactory;

public abstract class AbstractContainerRegistry<C extends Container> implements
		ContainerRegistry<C> {

	protected C container;

	private Scanner scanner;

	public AbstractContainerRegistry(C container) {
		this.container = container;
		this.scanner = new ReflectionsScanner(container);
	}

	protected abstract void registerCustomComponents();

	private void registerLocalComponentes() {

		container.registerSingleton(CacheManager.class.getName(), container
				.getSingletonBeanFactory(new CacheManagerComponentFactory(
						this.container)));

		container
				.registerSingleton(
						KeyValuesProvider.class.getName(),
						container
								.getSingletonBeanFactory(new KeyValuesProviderComponentFactory(
										this.container)));

		container.registerSingleton(Translator.class.getName(), container
				.getSingletonBeanFactory(new TranslatorComponentFactory(
						this.container)));

		container.registerSingleton(Application.class.getName(), container
				.getSingletonBeanFactory(new ApplicationComponentFactory(
						this.container)));

		container.registerSingleton(DataModelFactory.class.getName(), container
				.getSingletonBeanFactory(new DataModelComponentFactory(
						this.container)));

		BeanFactory<?, ?> bf = new RepositoryBeanFactory(this.container);
		ComponentFactory<BeanFactory<?, ?>> cf = new BeanFactoryComponentFactory<BeanFactory<?, ?>>(
				this.container, bf);

		container.addComponentFactory(cf);
		container.registerSingleton(bf.getClass().getName(), cf);

		bf = new ServiceBeanFactory(this.container);
		cf = new BeanFactoryComponentFactory<BeanFactory<?, ?>>(this.container,
				bf);

		container.addComponentFactory(cf);
		container.registerSingleton(bf.getClass().getName(), cf);
	}

	@Override
	public void deepRegister() throws ConfigurationException {

		container.deepRegister();

		ComponentScannerFactory scannerFactory = container
				.getComponentScannerFactory();

		Collection<ComponentScanner> scanners = scannerFactory
				.getComponentScanners();

		for (ComponentScanner componentScanner : scanners) {

			for (BeanMetaDataFactory<?, ?> property : this.container
					.getContainerConfig().getBeanMetaDataFactories()) {
				componentScanner.addMetaDataFactory(property);
			}
			componentScanner.scan(scanner, this.container);
		}
	}

	@Override
	public void register() throws ConfigurationException {

		this.configure();

		container.registerSingleton(Container.class.getName(), container);

		this.registerCustomComponents();

		this.registerLocalComponentes();

		this.deepRegister();
	}

	protected abstract void configure();
}
