package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.CacheManager;
import br.com.cd.mvo.DefaultKeyValuesProvider;
import br.com.cd.mvo.KeyValuesProvider;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;

public class KeyValuesProviderComponentFactory extends
		AbstractComponentFactory<KeyValuesProvider> {

	private CacheManager cacheManager;
	private long cacheTime = -1;
	private String[] suportedLocaleLanguages;
	private String defaultLocale;

	public KeyValuesProviderComponentFactory(Container container) {
		super(container);
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
			cacheTime = container.getInitApplicationConfig().getI18nCacheTime();

		return cacheTime;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = container.getInitApplicationConfig()
					.getSuportedLocales();

		return suportedLocaleLanguages;
	}

	private String getDefaultLocale() throws ConfigurationException {
		if (defaultLocale == null)
			defaultLocale = container.getInitApplicationConfig()
					.getDefaultLocale();

		return defaultLocale;
	}

	private CacheManager getCacheManager() throws NoSuchBeanDefinitionException {

		if (cacheManager == null)
			cacheManager = container.getBean(CacheManager.class);

		return cacheManager;
	}
}
