package br.com.cd.mvo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.scan.AnnotatedComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.EntityComponentScanner;
import br.com.cd.mvo.orm.PersistenceManagerFactory;

public class InitApplicationConfig {

	private String defaultLocale = ConfigParamKeys.DefaultValues.DEFAULT_LOCALE;
	private String[] suportedLocales = ConfigParamKeys.DefaultValues.SUPORTED_LOCALES
			.split(",");
	private long cacheManagerMaxSize = ConfigParamKeys.DefaultValues.CACHE_MANAGER_MAX_SIZE;
	private long i18nCacheTime = ConfigParamKeys.DefaultValues.I18N_CACHE_TIME;
	private String bundleName = ConfigParamKeys.DefaultValues.BUNDLE_NAME;

	private List<String> packagesToScan = new ArrayList<String>();
	private int initialPageSize = ConfigParamKeys.DefaultValues.INITIAL_PAGESIZE;
	private String scopeName = ConfigParamKeys.DefaultValues.SCOPE_NAME;

	private String persistenceManagerFactoryClass = ConfigParamKeys.DefaultValues.PERSISTENCE_MANAGER_FACTORY_CLASS;
	private String persistenceFactoryQualifier = ConfigParamKeys.DefaultValues.PERSISTENCE_FACTORY_QUALIFIER;

	public InitApplicationConfig(String... packagesToScan) {
		this.packagesToScan.addAll(Arrays.asList(packagesToScan));
	}

	public void setPackagesToScan(String packageToScan,
			String... packagesToScan) {
		this.packagesToScan = new ArrayList<String>();
		this.packagesToScan.add(packageToScan);
		this.packagesToScan.addAll(Arrays.asList(packagesToScan));
	}

	public void setPackagesToScan(List<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String getPersistenceManagerFactoryClass() {
		return persistenceManagerFactoryClass;
	}

	public void setPersistenceManagerFactoryClass(String className) {
		try {
			@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
			Class<?> type = (Class<? extends PersistenceManagerFactory>) Class
					.forName(className);
			this.persistenceManagerFactoryClass = className;
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new RuntimeException(new ConfigurationException(e));
		}
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String[] getSuportedLocales() {
		return suportedLocales;
	}

	public void setSuportedLocales(Collection<String> suportedLocales) {
		this.setSuportedLocales(suportedLocales
				.toArray(new String[suportedLocales.size()]));
	}

	public void setSuportedLocales(String... suportedLocales) {
		this.suportedLocales = suportedLocales;
	}

	public long getCacheManagerMaxSize() {
		return cacheManagerMaxSize;
	}

	public void setCacheManagerMaxSize(long cacheTime) {
		this.cacheManagerMaxSize = cacheTime;
	}

	public long getI18nCacheTime() {
		return i18nCacheTime;
	}

	public void setI18nCacheTime(long i18nCacheTime) {
		this.i18nCacheTime = i18nCacheTime;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public int getInitialPageSize() {
		return initialPageSize;
	}

	public void setInitialPageSize(int initialPageSize) {
		this.initialPageSize = initialPageSize;
	}

	public String getPersistenceFactoryQualifier() {
		return persistenceFactoryQualifier;
	}

	public void setPersistenceFactoryQualifier(
			String persistenceFactoryQualifier) {
		this.persistenceFactoryQualifier = persistenceFactoryQualifier;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public ComponentScannerFactory getComponentScannerFactory(
			Container container) throws ConfigurationException {

		// TODO: Implementar
		ComponentScannerFactory beanDiscoveryFactory;
		try {
			beanDiscoveryFactory = container
					.getBean(ComponentScannerFactory.class);
		} catch (NoSuchBeanDefinitionException e) {

			String packagesToScan = container.getContainerConfig()
					.getInitParameter(ConfigParamKeys.PACKAGES_TO_SCAN);
			if (packagesToScan.isEmpty()) {
				throw new ConfigurationException("No parameter '"
						+ ConfigParamKeys.PACKAGES_TO_SCAN + "' configured");
			}

			beanDiscoveryFactory = new ComponentScannerFactory(
					packagesToScan.split(","));
			beanDiscoveryFactory
					.addComponentScanner(new AnnotatedComponentScanner(
							packagesToScan.split(",")));
			beanDiscoveryFactory
					.addComponentScanner(new EntityComponentScanner(
							packagesToScan.split(",")));

			container.registerSingleton(beanDiscoveryFactory.getClass()
					.getName(), beanDiscoveryFactory);
		}

		return beanDiscoveryFactory;
	}

	public static InitApplicationConfig get(ContainerConfig<?> config)
			throws ConfigurationException {

		InitApplicationConfig applicationConfig = new InitApplicationConfig();
		applicationConfig.merge(config);

		return applicationConfig;
	}

	public void merge(ContainerConfig<?> config) throws ConfigurationException {

		String defaultLocale = config.getInitParameter(
				ConfigParamKeys.DEFAULT_LOCALE,
				ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);
		if (defaultLocale.isEmpty())
			this.setDefaultLocale(defaultLocale);

		String suportedLocales = config.getInitParameter(
				ConfigParamKeys.SUPORTED_LOCALES,
				ConfigParamKeys.DefaultValues.SUPORTED_LOCALES);
		if (suportedLocales.isEmpty())
			this.setSuportedLocales(suportedLocales.split(","));

		String bundleName = config.getInitParameter(
				ConfigParamKeys.BUNDLE_NAME,
				ConfigParamKeys.DefaultValues.BUNDLE_NAME);
		if (bundleName.isEmpty())
			this.setBundleName(bundleName);

		long cacheMaxTime = config.getInitParameter(
				ConfigParamKeys.CACHE_MANAGER_MAX_SIZE,
				ConfigParamKeys.DefaultValues.CACHE_MANAGER_MAX_SIZE);
		if (cacheMaxTime > 0)
			this.setCacheManagerMaxSize(cacheMaxTime);

		long cacheTime = config.getInitParameter(
				ConfigParamKeys.I18N_CACHE_TIME,
				ConfigParamKeys.DefaultValues.I18N_CACHE_TIME);
		if (cacheTime > 0)
			this.setI18nCacheTime(cacheTime);

		int pageSize = config.getInitParameter(
				ConfigParamKeys.INITIAL_PAGESIZE,
				ConfigParamKeys.DefaultValues.INITIAL_PAGESIZE);
		if (pageSize > 0)
			this.setInitialPageSize(pageSize);

		String qualifier = config.getInitParameter(
				ConfigParamKeys.PERSISTENCE_FACTORY_QUALIFIER,
				ConfigParamKeys.DefaultValues.PERSISTENCE_FACTORY_QUALIFIER);
		if (qualifier.isEmpty())
			this.setPersistenceFactoryQualifier(qualifier);

		String className = config
				.getInitParameter(
						ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_CLASS,
						ConfigParamKeys.DefaultValues.PERSISTENCE_MANAGER_FACTORY_CLASS);
		if (className.isEmpty())
			this.setPersistenceManagerFactoryClass(className);

		String scope = config.getInitParameter(ConfigParamKeys.SCOPE_NAME,
				ConfigParamKeys.DefaultValues.SCOPE_NAME);
		if (scope.isEmpty())
			this.setScopeName(scope);

	}
}