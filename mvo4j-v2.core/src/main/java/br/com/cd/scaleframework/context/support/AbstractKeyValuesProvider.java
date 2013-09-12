package br.com.cd.scaleframework.context.support;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.KeyValuesProvider;

public abstract class AbstractKeyValuesProvider implements KeyValuesProvider {

	public static final String BUNDLE_KEY = KeyValuesProvider.class.getName()
			+ "_BUNDLE_KEY_";
	public static final String I18N_KEY = KeyValuesProvider.class.getName()
			+ "_I18N_KEY_";
	public static final String SUPORTED_LOCALES_KEY = KeyValuesProvider.class
			.getName() + "_SUPORTED_LOCALES_KEY_";

	protected CacheManager cacheManager;

	public AbstractKeyValuesProvider(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	protected int getCacheTime() {
		return cacheManager.getCacheTime();
	}

	protected ResourceBundle getBundle(String bundleName, Locale locale) {

		String cacheKey = (BUNDLE_KEY + locale.toString() + "_" + bundleName);

		ResourceBundle bundle = cacheManager.getObject(ResourceBundle.class,
				cacheKey);
		if (bundle == null) {

			try {
				LoggerFactory.getLogger(KeyValuesProvider.class.getName())
						.debug("\nloading Bundle: {0}, Locale: {1}",
								new Object[] { bundleName, locale });

				bundle = ResourceBundle.getBundle(bundleName, locale);

				int cacheTime = this.getCacheTime();
				System.out.println("storing bundle in chache with cacheKey: "
						+ cacheKey + ", cacheTime: " + cacheTime);

				cacheManager.add(cacheKey, bundle, cacheTime);
			} catch (Exception e) {
				LoggerFactory.getLogger(KeyValuesProvider.class.getName())
						.error("\ncoud not load bundle: {0}, locale: {1}",
								new Object[] { bundleName, locale }, e);
			}
		}
		return bundle;
	}

	protected Locale setDefaultLocaleIfNecessary(Locale locale) {
		if (locale == null)
			locale = getDefaultLocale();

		return locale;
	}
}
