package br.com.cd.scaleframework.context.support;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import br.com.cd.scaleframework.config.ComponentScanner;
import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.ComponentFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.discovery.ComponentDiscovery;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;
import br.com.cd.scaleframework.core.discovery.ComponentFactoryConfig;
import br.com.cd.scaleframework.core.discovery.support.ComponentDiscoveryExecutor;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.core.support.DefaultComponentRegistryVisitor;
import br.com.cd.scaleframework.ioc.BeanFactoryFacade;
import br.com.cd.scaleframework.ioc.BeanFactoryFacadeAware;
import br.com.cd.scaleframework.ioc.BeanRegistryFacade;
import br.com.cd.scaleframework.util.GenericsUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {

	private List<BeanRegistryFacade> registries = new ArrayList<BeanRegistryFacade>();
	private List<BeanFactoryFacade> factories = new ArrayList<BeanFactoryFacade>();

	public AbstractApplicationContext(BeanRegistryFacade beanRegistry,
			BeanFactoryFacade beanFactory) {
		this.registries.add(beanRegistry);
		this.factories.add(beanFactory);
	}

	@Override
	public Configuration createConfiguration(FilterBuilder filter,
			Scanner... scanners) {

		Set<URL> urls = new HashSet<URL>();

		urls.addAll(ClasspathHelper.forJavaClassPath());
		urls.addAll(ClasspathHelper.forManifest());

		return new ConfigurationBuilder().filterInputsBy(filter).setUrls(urls)
				.setScanners(scanners);
	}

	@Override
	public void refresh() {

		this.scanComponents();

		this.scanDiscoveries();
	}

	@SuppressWarnings("unchecked")
	private void scanComponents() {

		@SuppressWarnings("rawtypes")
		Set<Class<? extends ComponentScanner>> factoriesAware = new Reflections(
				this.createConfiguration(new FilterBuilder(),
						new SubTypesScanner()))
				.getSubTypesOf(ComponentScanner.class);

		for (@SuppressWarnings("rawtypes")
		Class<? extends ComponentScanner> scanner : factoriesAware) {
			if (Modifier.isAbstract(scanner.getModifiers())) {
				continue;
			}
			System.out.println("found scanner '" + scanner.getName()
					+ "' attempt instantiate...");
			try {
				scanner.getConstructor().newInstance().init(this);
			} catch (Exception e) {
				throw new ConfigurationException("could not instantiate bean '"
						+ scanner.getName() + ".init("
						+ this.getClass().getName() + ")", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void scanDiscoveries() {

		List<ComponentDiscovery> discoveries = new LinkedList<ComponentDiscovery>();
		Set<Class<? extends ComponentDiscovery>> discoveryTypes = new Reflections(
				this.createConfiguration(new FilterBuilder(),
						new SubTypesScanner()))
				.getSubTypesOf(ComponentDiscovery.class);

		System.out.println("\nsearching sub-classes of '"
				+ ComponentDiscovery.class + "', classes: " + discoveryTypes);

		for (Class<? extends ComponentDiscovery> discoveryType : discoveryTypes) {

			if (Modifier.isAbstract(discoveryType.getModifiers())) {
				continue;
			}
			System.out.println("found '" + discoveryType.getName()
					+ "' attempt instantiate...");
			try {
				discoveries.add(discoveryType.getConstructor().newInstance());
			} catch (Exception e) {
				throw new ConfigurationException("could not instantiate bean '"
						+ discoveryType.getName() + ", "
						+ this.getClass().getName() + ")", e);
			}
		}

		ComponentDiscoveryVisitor visitor = new DefaultComponentRegistryVisitor();

		ComponentDiscoveryExecutor<ApplicationContext> executor = new ComponentDiscoveryExecutor<ApplicationContext>(
				visitor);

		@SuppressWarnings("rawtypes")
		Set<Class<? extends ComponentFactoryConfig>> factories = new Reflections(
				this.createConfiguration(new FilterBuilder(),
						new SubTypesScanner()))
				.getSubTypesOf(ComponentFactoryConfig.class);

		System.out.println("\nsearching sub-classes of '"
				+ ComponentFactoryConfig.class + "', classes: " + factories);

		for (@SuppressWarnings("rawtypes")
		Class<? extends ComponentFactoryConfig> factoryType : factories) {

			if (Modifier.isAbstract(factoryType.getModifiers())) {
				continue;
			}
			System.out.println("found '" + factoryType.getName()
					+ "' attempt instantiate...");
			try {
				@SuppressWarnings("rawtypes")
				ComponentFactoryConfig componentFactory = factoryType
						.getConstructor().newInstance();

				for (ComponentDiscovery discovery : discoveries) {
					executor.discover(componentFactory, discovery, this);
				}

			} catch (Exception e) {
				throw new ConfigurationException("could not instantiate bean '"
						+ factoryType.getName() + ", "
						+ this.getClass().getName() + ")", e);
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T checkBeansAware(T bean) {
		if (bean instanceof BeanFactoryFacadeAware) {
			if (!this.getClass().isAssignableFrom(
					GenericsUtils.getTypeArguments(
							BeanFactoryFacadeAware.class,
							((BeanFactoryFacadeAware) bean).getClass()).get(0))) {

				((BeanFactoryFacadeAware) bean).init(this);
			}
		}
		return bean;
	}

	@Override
	public Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException {

		try {
			return this.getComponent(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			for (BeanFactoryFacade beanFactory : factories) {
				if (beanFactory.containsBean(beanName)) {
					return checkBeansAware(beanFactory.getBean(beanName, args));
				}
			}
		}
		throw new NoSuchBeanDefinitionException(beanName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException, ClassCastException {

		try {
			ComponentProxy proxy = this.getComponent(beanName);
			if (beanType.isAssignableFrom(proxy.getClass())) {
				return (T) proxy;
			}
		} catch (NoSuchBeanDefinitionException e) {
			for (BeanFactoryFacade beanFactory : factories) {
				if (beanFactory.containsBean(beanName, beanType)) {
					return checkBeansAware(beanFactory.getBean(beanName,
							beanType, args));
				}
			}
		}
		throw new NoSuchBeanDefinitionException(beanName);
	}

	@Override
	public <T> T getBean(Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException {
		for (BeanFactoryFacade beanFactory : factories) {
			if (beanFactory.containsBean(beanType)) {
				return checkBeansAware(beanFactory.getBean(beanType, args));
			}
		}
		throw new NoSuchBeanDefinitionException(beanType);
	}

	@Override
	public void registerBean(Class<?> beanType) {
		for (BeanRegistryFacade beanRegistry : registries) {
			beanRegistry.registerBean(beanType);
		}
	}

	@Override
	public void registerBean(String beanName, Class<?> beanType) {
		for (BeanRegistryFacade beanRegistry : registries) {
			beanRegistry.registerBean(beanName, beanType);
		}
	}

	@Override
	public void overrideBean(Class<?> beanType, Class<?> overrideBeanType) {
		for (BeanRegistryFacade beanRegistry : registries) {
			beanRegistry.overrideBean(beanType, overrideBeanType);
		}
	}

	@Override
	public boolean containsBean(String beanName) {
		for (BeanFactoryFacade beanFactory : factories) {
			if (beanFactory.containsBean(beanName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsBean(Class<?> beanType) {
		for (BeanFactoryFacade beanFactory : factories) {
			if (beanFactory.containsBean(beanType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsBean(String beanName, Class<?> beanType) {
		for (BeanFactoryFacade beanFactory : factories) {
			if (beanFactory.containsBean(beanName, beanType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addBeanFactory(BeanFactoryFacade beanFactory) {
		this.factories.add(beanFactory);
	}

	@Override
	public void addBeanRegistry(BeanRegistryFacade beanRegistry) {
		this.registries.add(beanRegistry);
	}

	@SuppressWarnings("rawtypes")
	private List<ComponentFactory> componentFactories = new LinkedList<ComponentFactory>();

	@Override
	public void registerComponentFactory(
			@SuppressWarnings("rawtypes") ComponentFactory componentFactory) {
		componentFactories.add(componentFactory);
	}

	@SuppressWarnings({ "rawtypes" })
	// @Override
	public ComponentProxy getComponent(String componentName) {
		for (ComponentFactory componentFactory : componentFactories) {
			if (componentFactory.containsComponent(componentName)) {
				return componentFactory.getComponent(componentName, this);
			}
		}
		throw new NoSuchBeanDefinitionException(componentName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// @Override
	public <O extends ComponentObject, P extends ComponentProxy<O>> P getComponent(
			String componentName, Class<O> componentType) {
		ComponentProxy proxy = this.getComponent(componentName);
		if (componentType.isAssignableFrom(proxy.getComponent().getClass())) {
			return (P) proxy;
		}
		throw new NoSuchBeanDefinitionException(componentName);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	// @Override
	public <O extends ComponentObject, P extends ComponentProxy<O>> P getComponent(
			Class<?> targetEntity, Class<O> componentType) {
		for (ComponentFactory componentFactory : componentFactories) {
			if (componentFactory.containsComponent(targetEntity)) {
				ComponentProxy proxy = componentFactory.getComponent(
						targetEntity, this);
				if (componentType.isAssignableFrom(proxy.getComponent()
						.getClass())) {
					return (P) proxy;
				}
			}
		}
		throw new NoSuchBeanDefinitionException("", targetEntity.getName());

	}

}
