package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.util.Collection;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.ApplicationDynamicBeanFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.support.CacheManagerDynamicBeanFactory;
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
	private ComponentFactoryContainer container;

	public SpringBeanRegistry(ComponentFactoryContainer container) {
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

	public void registerComponent(Class<?> beanType, String beanName) {
		RootBeanDefinition definition = new RootBeanDefinition(beanType);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		definition.getPropertyValues().addPropertyValue("order",
				Ordered.LOWEST_PRECEDENCE);
		((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(beanName,
				definition);
	}

	private void registerSingletons() {

		beanFactory.registerSingleton(CacheManagerDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<CacheManager>(
				new CacheManagerDynamicBeanFactory(this.container)));

		beanFactory.registerSingleton(KeyValuesProviderDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<KeyValuesProvider>(
				new KeyValuesProviderDynamicBeanFactory(this.container)));

		beanFactory.registerSingleton(TranslatorDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<Translator>(
				new TranslatorDynamicBeanFactory(this.container)));

		beanFactory.registerSingleton(ApplicationDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<Application>(
				new ApplicationDynamicBeanFactory(this.container)));

		beanFactory.registerSingleton(DataModelDynamicBeanFactory.class
				.getName(), new ComponentFactoryBean<DataModelFactory>(
				new DataModelDynamicBeanFactory(this.container)));

	}

	private void registerDynamicBeans() throws ConfigurationException {

		DynamicBeanDiscoveryFactory discoveryFactory = container
				.getDiscoveryFactory();

		Collection<DynamicBeanDiscovery> discoveries = discoveryFactory
				.getDiscoveries();

		for (DynamicBeanDiscovery discovery : discoveries) {

			Collection<DynamicBean<?>> candidates = discovery.getCandidates();

			for (DynamicBean<?> dynamicBean : candidates) {
				try {
					this.registerOn(dynamicBean);
				} catch (NoSuchBeanDefinitionException e) {
					throw new ConfigurationException(e);
				}
			}
		}
	}

	public void registerOn(Class<?> type, DynamicBean<?> beanConfig) {
		AnnotatedGenericBeanDefinition definition = new AnnotatedGenericBeanDefinition(
				type);
		definition.setLazyInit(true);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
		definition.setRole(BeanDefinition.ROLE_APPLICATION);

		ScopeMetadata scopeMetadata = new ScopeMetadata();
		scopeMetadata.setScopeName(beanConfig.getBeanConfig().scope());

		String beanName = container.generateBeanName(beanConfig);

		BeanDefinitionHolder definitionHolder = applyScopeOn(
				new BeanDefinitionHolder(definition, beanName), scopeMetadata);

		/*
		 * ConstructorArgumentValues constructorArgumentValues = new
		 * ConstructorArgumentValues();
		 * 
		 * ConstructorArgumentValues.ValueHolder holder = new
		 * DymanicValueHolder();
		 * 
		 * constructorArgumentValues.addGenericArgumentValue(holder);
		 * 
		 * definition.setConstructorArgumentValues(constructorArgumentValues);
		 */

		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder,
				(BeanDefinitionRegistry) beanFactory);
	}

	/*
	 * class DymanicValueHolder extends ConstructorArgumentValues.ValueHolder {
	 * 
	 * public DymanicValueHolder() { super(null); }
	 * 
	 * @Override public Object getValue() { // TODO Auto-generated method stub
	 * return super.getValue(); }
	 * 
	 * @Override public String getType() { // TODO Auto-generated method stub
	 * return super.getType(); }
	 * 
	 * }
	 */

	private BeanDefinitionHolder applyScopeOn(BeanDefinitionHolder definition,
			ScopeMetadata scopeMetadata) {
		String scope = scopeMetadata.getScopeName();
		ScopedProxyMode proxyMode = scopeMetadata.getScopedProxyMode();
		definition.getBeanDefinition().setScope(scope);
		if (BeanDefinition.SCOPE_SINGLETON.equals(scope)
				|| BeanDefinition.SCOPE_PROTOTYPE.equals(scope)
				|| proxyMode.equals(ScopedProxyMode.NO)) {
			return definition;
		} else {
			boolean proxyTargetClass = proxyMode
					.equals(ScopedProxyMode.TARGET_CLASS);
			return ScopedProxyUtils.createScopedProxy(definition,
					(BeanDefinitionRegistry) beanFactory, proxyTargetClass);
		}
	}

	public void registerOn(DynamicBean<?> beanConfig)
			throws NoSuchBeanDefinitionException {

		this.registerSingletons();

		String beanName = SpringBeanRegistry.resolveBeanConfigName(beanConfig
				.getTargetBean(), beanConfig.getBeanConfig().targetEntity());

		beanFactory.registerSingleton(beanName, beanConfig);

		this.registerOn(container.createComponentProxy(beanConfig), beanConfig);
	}

	public static String resolveBeanConfigName(Class targetBean,
			Class targetEntity) {

		return targetBean.getName() + "_" + targetEntity.getName();
	}

	public void register() throws ConfigurationException {

		beanFactory.registerSingleton(
				ComponentFactoryContainer.class.getName(), container);
		this.registerComponent(InjectionBeanPostProcessor.class,
				AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME);

		beanFactory.registerSingleton(
				GenericAutowireCandidateResolver.class.getName(),
				new GenericAutowireCandidateResolver(container));

		this.registerComponent(CustomAutowireConfigurer.class,
				CustomAutowireConfigurer.class.getName());

		// this.registerComponent(GenericAutowireCandidateResolver.class);

		this.registerSingletons();

		this.registerDynamicBeans();

	}
}
