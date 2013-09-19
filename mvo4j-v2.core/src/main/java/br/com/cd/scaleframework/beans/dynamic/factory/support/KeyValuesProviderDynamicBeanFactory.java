package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.support.DefaultKeyValuesProvider;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class KeyValuesProviderDynamicBeanFactory extends
		AbstractDynamicBeanFactory<KeyValuesProvider> {

	private CacheManager cacheManager;
	private long cacheTime = -1;
	private String[] suportedLocaleLanguages;
	private String defaultLocale;
	private String bundleName;

	public KeyValuesProviderDynamicBeanFactory(ComponentFactoryContainer container) {
		super(container);
	}

	@Override
	public Class<KeyValuesProvider> getObjectType() {
		return KeyValuesProvider.class;
	}

	@Override
	protected KeyValuesProvider getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		try {
			return new DefaultKeyValuesProvider(getCacheManager(),
					getCacheTime(), getDefaultLocale(),
					getSuportedLocaleLanguages());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	private long getCacheTime() throws ConfigurationException {
		if (cacheTime == -1)
			cacheTime = getContainer().getDiscoveryFactory().getI18nCacheTime();

		return cacheTime;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = getContainer().getDiscoveryFactory()
					.getSuportedLocales();

		return suportedLocaleLanguages;
	}

	private String getDefaultLocale() throws ConfigurationException {
		if (defaultLocale == null)
			defaultLocale = getContainer().getDiscoveryFactory()
					.getDefaultLocale();

		return defaultLocale;
	}

	private CacheManager getCacheManager() throws NoSuchBeanDefinitionException {

		if (cacheManager == null)
			cacheManager = getContainer().getBean(CacheManager.class);

		return cacheManager;
	}
}
