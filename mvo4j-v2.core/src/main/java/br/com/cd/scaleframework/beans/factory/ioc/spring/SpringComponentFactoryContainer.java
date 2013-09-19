package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.factory.ioc.BeanConfigNameGenerator;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.ConfigParamKeys;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.DynamicBean;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;
import br.com.cd.scaleframework.web.util.WebUtil;

public class SpringComponentFactoryContainer implements
		ComponentFactoryContainer {

	private ServletContext servletContext;
	private ConfigurableWebApplicationContext parentApplicationContext;
	private BeanConfigNameGenerator beanConfigNameGenerator = new BeanConfigNameGenerator();

	public SpringComponentFactoryContainer(
			ConfigurableWebApplicationContext parentApplicationContext,
			ServletContext servletContext) {

		this.parentApplicationContext = parentApplicationContext;
		this.servletContext = servletContext;
	}

	@Override
	public void start() {

		parentApplicationContext.setServletContext(servletContext);
		parentApplicationContext
				.addBeanFactoryPostProcessor(new SpringBeanRegistry(this));
		parentApplicationContext.refresh();
		parentApplicationContext.start();

		this.deepRegister();
	}

	@Override
	public void stop() {
		parentApplicationContext.stop();
	}

	@Override
	public boolean containsBean(String beanName) {
		return parentApplicationContext.containsBean(beanName);
	}

	@Override
	public Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException {

		try {
			return parentApplicationContext.getBean(beanName, args);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException {

		Object bean;
		try {
			bean = parentApplicationContext.getBean(beanName, args);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}

		try {
			return (T) bean;
		} catch (ClassCastException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public <T> Collection<T> getBeansOfType(Class<T> beanType)
			throws NoSuchBeanDefinitionException {
		try {
			return parentApplicationContext.getBeansOfType(beanType).values();
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public <T> T getBean(Class<T> beanType)
			throws NoSuchBeanDefinitionException {
		try {
			return parentApplicationContext.getBean(beanType);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public DynamicBeanDiscoveryFactory getDiscoveryFactory()
			throws ConfigurationException {

		DynamicBeanDiscoveryFactory beanDiscoveryFactory;
		try {
			beanDiscoveryFactory = this
					.getBean(DynamicBeanDiscoveryFactory.class);
		} catch (NoSuchBeanDefinitionException e) {

			String packagesToScan = WebUtil.getInitParameter(servletContext,
					PACKAGES_TO_SCAN_PARAM_NAME);
			if (packagesToScan.isEmpty()) {
				throw new ConfigurationException("No parameter '"
						+ PACKAGES_TO_SCAN_PARAM_NAME + "' configured");
			}

			beanDiscoveryFactory = new DynamicBeanDiscoveryFactory(
					packagesToScan);
		}

		String defaultLocale = WebUtil.getInitParameter(servletContext,
				ConfigParamKeys.DEFAULT_LOCALE,
				ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);
		if (defaultLocale.isEmpty())
			beanDiscoveryFactory.setDefaultLocale(defaultLocale);

		String suportedLocales = WebUtil.getInitParameter(servletContext,
				ConfigParamKeys.SUPORTED_LOCALES,
				ConfigParamKeys.DefaultValues.SUPORTED_LOCALES);
		if (suportedLocales.isEmpty())
			beanDiscoveryFactory.setSuportedLocales(suportedLocales.split(","));

		String bundleName = WebUtil.getInitParameter(servletContext,
				ConfigParamKeys.BUNDLE_NAME,
				ConfigParamKeys.DefaultValues.BUNDLE_NAME);
		if (bundleName.isEmpty())
			beanDiscoveryFactory.setBundleName(bundleName);

		long cacheMaxTime = WebUtil.getInitParameter(servletContext,
				ConfigParamKeys.CACHE_MANAGER_MAX_SIZE,
				ConfigParamKeys.DefaultValues.CACHE_MANAGER_MAX_SIZE);
		if (cacheMaxTime > 0)
			beanDiscoveryFactory.setCacheManagerMaxSize(cacheMaxTime);

		long cacheTime = WebUtil.getInitParameter(servletContext,
				ConfigParamKeys.I18N_CACHE_TIME,
				ConfigParamKeys.DefaultValues.I18N_CACHE_TIME);
		if (cacheTime > 0)
			beanDiscoveryFactory.setI18nCacheTime(cacheTime);

		return beanDiscoveryFactory;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	private Collection<ComponentFactory<?>> componentFactories = Collections
			.emptySet();

	private Map<String, Object> singletonObjects = Collections.emptyMap();
	private Collection<BeanDefinitionHolder> beanDefinitionHolderList = Collections
			.emptyList();
	private Map<String, BeanDefinition> beanDefinitionList = Collections
			.emptyMap();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Class<T> createComponentProxy(
			DynamicBeanManager<? extends BeanConfig<T, ?>> beanConfig)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.createComponentProxy(beanConfig);
			}
		}
		throw new NoSuchBeanDefinitionException(beanConfig.getBeanConfig()
				.toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T, ID extends Serializable> DynamicBean<T, ID> getDynamicBean(
			DynamicBeanManager<? extends BeanConfig<T, ID>> beanConfig)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.getDynamicBean(beanConfig);
			}
		}
		throw new NoSuchBeanDefinitionException(beanConfig.getBeanConfig()
				.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, ID extends Serializable, Config extends BeanConfig<T, ID>> DynamicBean<T, ID> getDynamicBean(
			Class<T> targetEntity, Class<ID> entityIdType,
			Class<Config> beanConfigType) throws NoSuchBeanDefinitionException {

		String beanName = this.generateBeanConfigName(targetEntity,
				entityIdType);

		Object bean = this.getBean(beanName);
		if (bean instanceof DynamicBeanManager) {
			try {
				return this.getDynamicBean((DynamicBeanManager<Config>) bean);
			} catch (ClassCastException e) {
				//
			}
		}
		throw new NoSuchBeanDefinitionException("Could not convert '" + bean
				+ "' to '" + DynamicBeanManager.class.getCanonicalName() + "<"
				+ BeanConfig.class.getCanonicalName() + "<"
				+ targetEntity.getName() + ", " + entityIdType.getName() + ">"
				+ ">'");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String generateBeanName(DynamicBeanManager<?> beanConfig) {

		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.generateBeanName(beanConfig);
			}
		}
		return beanConfig.getBeanConfig().name() + "Bean";
	}

	@Override
	public String generateBeanConfigName(Class<?> targetBean,
			Class<?> targetEntity) {

		return beanConfigNameGenerator.generateBeanName(targetBean,
				targetEntity);
	}

	@Override
	public void registerSingleton(DynamicBeanManager beanManager) {

		String beanName = this.generateBeanConfigName(beanManager
				.getTargetBean(), beanManager.getBeanConfig().targetEntity());

		this.registerSingleton(beanName, beanManager);
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) {

		if (parentApplicationContext.isActive()) {
			parentApplicationContext.getBeanFactory().registerSingleton(
					beanName, singletonObject);
		} else {
			this.singletonObjects.put(beanName, singletonObject);
		}
	}

	@Override
	public void registerBean(DynamicBeanManager beanManager)
			throws NoSuchBeanDefinitionException {

		this.registerOn(this.createComponentProxy(beanManager), beanManager);
	}

	private void registerOn(Class<?> type, DynamicBeanManager<?> beanConfig) {

		this.registerOn(type, this.generateBeanName(beanConfig), beanConfig
				.getBeanConfig().scope());
	}

	private void registerOn(Class<?> type, String beanName, String scopeName) {
		AnnotatedGenericBeanDefinition definition = new AnnotatedGenericBeanDefinition(
				type);
		definition.setLazyInit(true);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
		definition.setRole(BeanDefinition.ROLE_APPLICATION);

		ScopeMetadata scopeMetadata = new ScopeMetadata();
		scopeMetadata.setScopeName(scopeName);

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

		if (parentApplicationContext.isActive()) {
			BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder,
					(BeanDefinitionRegistry) parentApplicationContext
							.getBeanFactory());
		} else {
			beanDefinitionHolderList.add(definitionHolder);
		}
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
					(BeanDefinitionRegistry) parentApplicationContext
							.getBeanFactory(), proxyTargetClass);
		}
	}

	public void registerComponent(Class<?> beanType, String beanName) {
		RootBeanDefinition definition = new RootBeanDefinition(beanType);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		definition.getPropertyValues().addPropertyValue("order",
				Ordered.LOWEST_PRECEDENCE);

		if (parentApplicationContext.isActive()) {
			((BeanDefinitionRegistry) parentApplicationContext.getBeanFactory())
					.registerBeanDefinition(beanName, definition);
		} else {
			beanDefinitionList.put(beanName, definition);
		}
	}

	@Override
	public void deepRegister() {

		for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionList
				.entrySet()) {
			((BeanDefinitionRegistry) parentApplicationContext.getBeanFactory())
					.registerBeanDefinition(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Object> entry : this.singletonObjects.entrySet()) {
			parentApplicationContext.getBeanFactory().registerSingleton(
					entry.getKey(), entry.getValue());
		}
		for (BeanDefinitionHolder beanDefinitionHolder : this.beanDefinitionHolderList) {
			BeanDefinitionReaderUtils.registerBeanDefinition(
					beanDefinitionHolder,
					(BeanDefinitionRegistry) parentApplicationContext
							.getBeanFactory());
		}
	}

}