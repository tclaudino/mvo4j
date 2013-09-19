package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.support.ApplicationDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.CacheManagerDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.ComponentDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.ControllerComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.CrudControllerComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.DataModelDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.KeyValuesProviderDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.TranslatorDynamicBeanFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class SpringBeanRegistry implements BeanFactoryPostProcessor {

	// private final ScopeMetadataResolver scopeResolver = new
	// DynamicBeanScopeResolver();
	private ConfigurableListableBeanFactory beanFactory;
	private SpringComponentFactoryContainer container;

	public SpringBeanRegistry(SpringComponentFactoryContainer container) {
		this.container = container;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {

		this.beanFactory = factory;
		try {
			this.register();
		} catch (ConfigurationException e) {
			throw new BeansException(e.getMessage(), e) {
			};
		}
	}

	private void registerSingletons() {

		container.registerSingleton(CacheManagerDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<CacheManager>(
				new CacheManagerDynamicBeanFactory(this.container)));

		container.registerSingleton(KeyValuesProviderDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<KeyValuesProvider>(
				new KeyValuesProviderDynamicBeanFactory(this.container)));

		container.registerSingleton(TranslatorDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<Translator>(
				new TranslatorDynamicBeanFactory(this.container)));

		container.registerSingleton(ApplicationDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<Application>(
				new ApplicationDynamicBeanFactory(this.container)));

		container.registerSingleton(
				DataModelDynamicBeanFactory.class.getName(),
				new ComponentFactoryBean<DataModelFactory>(
						new DataModelDynamicBeanFactory(this.container)));

		container
				.registerSingleton(
						ControllerComponentFactory.class.getName(),
						new ComponentFactoryBean<ControllerComponentFactory>(
								new ComponentDynamicBeanFactory<ControllerComponentFactory>(
										this.container,
										new ControllerComponentFactory(
												container))));

		container
				.registerSingleton(
						CrudControllerComponentFactory.class.getName(),
						new ComponentFactoryBean<CrudControllerComponentFactory>(
								new ComponentDynamicBeanFactory<CrudControllerComponentFactory>(
										this.container,
										new CrudControllerComponentFactory(
												container))));

	}

	private void registerDynamicBeans() throws ConfigurationException {

		DynamicBeanDiscoveryFactory discoveryFactory = container
				.getDiscoveryFactory();

		Collection<DynamicBeanDiscovery> discoveries = discoveryFactory
				.getDiscoveries();

		for (DynamicBeanDiscovery discovery : discoveries) {

			Collection<DynamicBeanManager<?>> candidates = discovery
					.getCandidates();

			for (DynamicBeanManager<?> dynamicBean : candidates) {
				try {
					this.registerOn(dynamicBean);
				} catch (NoSuchBeanDefinitionException e) {
					throw new ConfigurationException(e);
				}
			}

		}
	}

	@SuppressWarnings({ "rawtypes" })
	public void registerOn(DynamicBeanManager beanManager)
			throws NoSuchBeanDefinitionException {

		this.registerSingletons();

		container.registerSingleton(beanManager);
		container.registerBean(beanManager);
	}

	public void register() throws ConfigurationException {

		container.registerSingleton(ComponentFactoryContainer.class.getName(),
				container);

		container.registerComponent(InjectionBeanPostProcessor.class,
				AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME);

		// container.registerSingleton(
		// GenericAutowireCandidateResolver.class.getName(),
		// new GenericAutowireCandidateResolver(container));
		container.registerComponent(GenericAutowireCandidateResolver.class,
				GenericAutowireCandidateResolver.class.getName());

		this.registerSingletons();

		this.registerDynamicBeans();
	}
}
