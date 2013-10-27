package br.com.cd.mvo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.Proxifier;
import br.com.cd.mvo.ioc.scan.AnnotatedComponentScanner;
import br.com.cd.mvo.ioc.scan.ComponentScannerFactory;
import br.com.cd.mvo.ioc.scan.EntityComponentScanner;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.util.ParserUtils;

public class ApplicationConfig {

	public static final String BEAN_NAME = ApplicationConfig.class.getName();

	private Map<String, Object> parameters = new HashMap<>();

	private List<String> packagesToScan = new ArrayList<String>();

	public ApplicationConfig(String... packagesToScan) {
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

	@SuppressWarnings("rawtypes")
	private Class<RepositoryFactory> repositoryFactoryClass;

	@SuppressWarnings("rawtypes")
	public Class<RepositoryFactory> getRepositoryFactoryClass() {
		return repositoryFactoryClass;
	}

	public void setRepositoryFactoryClass(String className) {
		setRepositoryFactoryClass(loadClass(className, RepositoryFactory.class));
	}

	public void setRepositoryFactoryClass(
			@SuppressWarnings("rawtypes") Class<RepositoryFactory> classType) {
		this.repositoryFactoryClass = classType;
	}

	private Class<Proxifier> proxifierClass;

	public Class<Proxifier> getProxifierClass() {
		return proxifierClass;
	}

	public void setProxifierClass(String className) {
		setProxifierClass(loadClass(className, Proxifier.class));
	}

	public void setProxifierClass(Class<Proxifier> classType) {
		this.proxifierClass = classType;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> loadClass(String className, Class<T> classType) {
		try {
			return (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new RuntimeException(new ConfigurationException(e));
		}
	}

	public String getPersistenceManagerFactoryBeanName() {
		return this.get(ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME);
	}

	public void setPersistenceManagerFactoryBeanName(
			String persistenceManagerFactoryBeanName) {
		this.parameters.put(
				ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME,
				persistenceManagerFactoryBeanName);
	}

	public String getDefaultLocale() {
		return this.get(ConfigParamKeys.DEFAULT_LOCALE);
	}

	public void setDefaultLocale(String defaultLocale) {
		this.parameters.put(ConfigParamKeys.DEFAULT_LOCALE, defaultLocale);
	}

	public String[] getSuportedLocales() {
		return this.get(ConfigParamKeys.SUPORTED_LOCALES, String[].class);
	}

	public void setSuportedLocales(Collection<String> suportedLocales) {
		this.setSuportedLocales(suportedLocales
				.toArray(new String[suportedLocales.size()]));
	}

	public void setSuportedLocales(String... suportedLocales) {
		this.parameters.put(ConfigParamKeys.SUPORTED_LOCALES, suportedLocales);
	}

	public long getCacheManagerMaxSize() {
		return this.get(ConfigParamKeys.CACHE_MANAGER_MAX_SIZE, Long.class);
	}

	public void setCacheManagerMaxSize(long cacheTime) {
		this.parameters.put(ConfigParamKeys.CACHE_MANAGER_MAX_SIZE, cacheTime);
	}

	public long getI18nCacheTime() {
		return this.get(ConfigParamKeys.I18N_CACHE_TIME, Long.class);
	}

	public void setI18nCacheTime(long i18nCacheTime) {
		this.parameters.put(ConfigParamKeys.I18N_CACHE_TIME, i18nCacheTime);
	}

	public String getBundleName() {
		return this.get(ConfigParamKeys.BUNDLE_NAME);
	}

	public void setBundleName(String bundleName) {
		this.parameters.put(ConfigParamKeys.BUNDLE_NAME, bundleName);
	}

	public int getInitialPageSize() {
		return this.get(ConfigParamKeys.INITIAL_PAGESIZE, Integer.class);
	}

	public void setInitialPageSize(int initialPageSize) {
		this.parameters.put(ConfigParamKeys.INITIAL_PAGESIZE, initialPageSize);
	}

	public String getDefaultScopeName() {
		return this.get(ConfigParamKeys.DEFAULT_SCOPE_NAME);
	}

	public void setDefaultScopeName(String scopeName) {
		this.parameters.put(ConfigParamKeys.DEFAULT_SCOPE_NAME, scopeName);
	}

	public ComponentScannerFactory getComponentScannerFactory(
			Container container) throws ConfigurationException {

		ComponentScannerFactory beanDiscoveryFactory;
		try {
			beanDiscoveryFactory = container.getBean(
					ComponentScannerFactory.BEAN_NAME,
					ComponentScannerFactory.class);
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

			container.registerSingleton(ComponentScannerFactory.BEAN_NAME,
					beanDiscoveryFactory);
		}

		return beanDiscoveryFactory;
	}

	public static ApplicationConfig get(ContainerConfig<?> config)
			throws ConfigurationException {

		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.merge(config);

		return applicationConfig;
	}

	public void merge(ContainerConfig<?> config) throws ConfigurationException {

		String defaultLocale = config.getInitParameter(
				ConfigParamKeys.DEFAULT_LOCALE,
				ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);
		if (!defaultLocale.isEmpty())
			this.setDefaultLocale(defaultLocale);

		String suportedLocales = config.getInitParameter(
				ConfigParamKeys.SUPORTED_LOCALES,
				ConfigParamKeys.DefaultValues.SUPORTED_LOCALES);
		if (!suportedLocales.isEmpty())
			this.setSuportedLocales(suportedLocales.split(","));

		String bundleName = config.getInitParameter(
				ConfigParamKeys.BUNDLE_NAME,
				ConfigParamKeys.DefaultValues.BUNDLE_NAME);
		if (!bundleName.isEmpty())
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

		String persistenceFactoryBeanName = config
				.getInitParameter(
						ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME,
						ConfigParamKeys.DefaultValues.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME);
		if (!persistenceFactoryBeanName.isEmpty())
			this.setPersistenceManagerFactoryBeanName(persistenceFactoryBeanName);

		String className = config.getInitParameter(
				ConfigParamKeys.REPOSITORY_FACTORY_CLASS,
				ConfigParamKeys.DefaultValues.REPOSITORY_FACTORY_CLASS);
		if (!className.isEmpty())
			this.setRepositoryFactoryClass(className);

		String scope = config.getInitParameter(
				ConfigParamKeys.DEFAULT_SCOPE_NAME,
				ConfigParamKeys.DefaultValues.SCOPE_NAME);
		if (!scope.isEmpty())
			this.setDefaultScopeName(scope);

		String beanInstantiationStrategy = config
				.getInitParameter(
						ConfigParamKeys.BEAN_INSTANTIATION_STRATEGY_CLASS,
						ConfigParamKeys.DefaultValues.BEAN_INSTANTIATION_STRATEGY_CLASS);
		if (!beanInstantiationStrategy.isEmpty())
			this.setProxifierClass(beanInstantiationStrategy);

	}

	public String get(String parameterName) {
		return ParserUtils.parseString(parameters.get(parameterName));
	}

	public <T> T get(String parameterName, Class<T> parameterType) {
		return ParserUtils.parseObject(parameterType,
				parameters.get(parameterName));
	}
}