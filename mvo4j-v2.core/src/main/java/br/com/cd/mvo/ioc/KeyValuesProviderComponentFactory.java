package br.com.cd.mvo.ioc;

import br.com.cd.mvo.CacheManager;
import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.DefaultKeyValuesProvider;
import br.com.cd.mvo.core.KeyValuesProvider;

public class KeyValuesProviderComponentFactory extends AbstractComponentFactory<KeyValuesProvider> {

	private CacheManager cacheManager;
	private long cacheTime = -1;
	private String[] suportedLocaleLanguages;
	private String defaultLocale;

	public KeyValuesProviderComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected KeyValuesProvider getInstanceInternal() throws NoSuchBeanDefinitionException {

		try {
			return new DefaultKeyValuesProvider(getCacheManager(), getCacheTime(), getDefaultLocale(), getSuportedLocaleLanguages());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	protected String getComponentBeanName() {
		return KeyValuesProvider.BEAN_NAME;
	}

	private long getCacheTime() throws ConfigurationException {
		if (cacheTime == -1)
			cacheTime = container.getContainerConfig().getInitParameter(ConfigParamKeys.I18N_CACHE_TIME, ConfigParamKeys.DefaultValues.I18N_CACHE_TIME);

		return cacheTime;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = container.getContainerConfig()
					.getInitParameter(ConfigParamKeys.SUPORTED_LOCALES, ConfigParamKeys.DefaultValues.SUPORTED_LOCALES).split(",");

		return suportedLocaleLanguages;
	}

	private String getDefaultLocale() throws ConfigurationException {
		if (defaultLocale == null)
			defaultLocale = container.getContainerConfig().getInitParameter(ConfigParamKeys.DEFAULT_LOCALE, ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);

		return defaultLocale;
	}

	private CacheManager getCacheManager() throws NoSuchBeanDefinitionException {

		if (cacheManager == null)
			cacheManager = container.getBean(CacheManager.BEAN_NAME, CacheManager.class);

		return cacheManager;
	}
}
