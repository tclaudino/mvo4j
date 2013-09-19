package br.com.cd.scaleframework.beans.factory.ioc.spring;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.cd.scaleframework.beans.dynamic.factory.ComponentFactory;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscoveryFactory;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.ConfigParamKeys;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;
import br.com.cd.scaleframework.web.util.WebUtil;

public class SpringComponentFactoryContainer implements
		ComponentFactoryContainer {

	private ServletContext servletContext;
	private ConfigurableWebApplicationContext parentApplicationContext;

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

	private Collection<ComponentFactory> componentFactories = Collections
			.emptySet();

	@Override
	public Object getComponent(DynamicBean<?> beanConfig)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.getComponent(beanConfig);
			}
		}
		throw new NoSuchBeanDefinitionException(beanConfig.getBeanConfig()
				.toString());
	}

	@Override
	public Class<?> createComponentProxy(DynamicBean<?> beanConfig)
			throws NoSuchBeanDefinitionException {

		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.createComponentProxy(beanConfig);
			}
		}
		throw new NoSuchBeanDefinitionException(beanConfig.getBeanConfig()
				.toString());
	}

	@Override
	public String generateBeanName(DynamicBean<?> beanConfig) {
		for (ComponentFactory componentFactory : componentFactories) {

			if (componentFactory.isComponentCandidate(beanConfig)) {
				return componentFactory.generateBeanName(beanConfig);
			}
		}
		return beanConfig.getBeanConfig().name() + "Bean";
	}

}