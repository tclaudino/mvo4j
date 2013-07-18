package br.com.cd.scaleframework.core.discovery.support;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.discovery.ComponentDiscovery;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoverySupport;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.core.support.DefaultComponentRegistryVisitor;

@SuppressWarnings("rawtypes")
public class ComponentDiscoveryExecutor<C extends ApplicationContext> {

	private final ComponentDiscoveryVisitor registryVisitor;
	private ComponentFactorySupport factorySupport;
	private C applicationContext;

	public ComponentDiscoveryExecutor(ComponentDiscoveryVisitor registryVisitor) {
		this.registryVisitor = registryVisitor;
	}

	public ComponentDiscoveryExecutor() {
		this(new DefaultComponentRegistryVisitor());
	}

	public <O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> void discover(
			ComponentFactoryConfig<O, P, A, F, S> componentFactory,
			ComponentDiscovery discovery, C applicationContext) {

		this.applicationContext = applicationContext;

		this.createFactorySupport();

		System.out
				.println("\nDynamicBeanDefinitionLoader.init, applicationContext: "
						+ applicationContext);

		discovery.discover(componentFactory.getComponentFactory(),
				componentFactory.getDiscoverySupport(), applicationContext,
				factorySupport, registryVisitor);

	}

	private void createFactorySupport() {

		if (this.factorySupport == null) {
			synchronized (this) {
				if (this.factorySupport == null) {
					try {
						this.factorySupport = applicationContext
								.getBean(ComponentFactorySupport.class);

					} catch (NoSuchBeanDefinitionException e) {
						this.factorySupport = new ComponentFactorySupport();
					}
				}
			}
		}
	}

}
