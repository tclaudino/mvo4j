package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.scan.ComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.ReflectionsScanner;
import br.com.cd.mvo.ioc.scan.Scanner;
import br.com.cd.mvo.ioc.support.ApplicationComponentFactory;
import br.com.cd.mvo.ioc.support.CacheManagerComponentFactory;
import br.com.cd.mvo.ioc.support.DataModelComponentFactory;
import br.com.cd.mvo.ioc.support.KeyValuesProviderComponentFactory;
import br.com.cd.mvo.ioc.support.RepositoryBeanFactory;
import br.com.cd.mvo.ioc.support.RepositoryListenerBeanFactory;
import br.com.cd.mvo.ioc.support.ServiceBeanFactory;
import br.com.cd.mvo.ioc.support.ServiceListenerBeanFactory;
import br.com.cd.mvo.ioc.support.TranslatorComponentFactory;

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

	private void registerLocalComponentes() {

		container.registerBean(new CacheManagerComponentFactory(this.container));
		container.registerBean(new KeyValuesProviderComponentFactory(this.container));
		container.registerBean(new TranslatorComponentFactory(this.container));
		container.registerBean(new ApplicationComponentFactory(this.container));
		container.registerBean(new DataModelComponentFactory(this.container));

		container.addComponentFactory(new RepositoryBeanFactory(this.container));
		container.addComponentFactory(new RepositoryListenerBeanFactory(this.container));
		container.addComponentFactory(new ServiceBeanFactory(this.container));
		container.addComponentFactory(new ServiceListenerBeanFactory(this.container));

		container.registerBean(Proxifier.BEAN_NAME, container.getApplicationConfig().getProxifierClass());
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
