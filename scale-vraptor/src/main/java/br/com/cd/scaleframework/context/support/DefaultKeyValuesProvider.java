package br.com.cd.scaleframework.context.support;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;

import br.com.cd.scaleframework.context.CacheManager;

public class DefaultKeyValuesProvider extends AbstractKeyValuesProvider {

	public DefaultKeyValuesProvider(CacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	public Map<String, String> getKeyValues(String bundleName) {

		Map<String, String> map = new HashMap<String, String>();

		for (Locale locale : this.getSupportedLocales()) {
			map.putAll(this.getKeyValues(bundleName, locale));
		}
		return map;
	}

	@Override
	public Map<String, String> getKeyValues(String bundleName, Locale locale) {
		locale = setDefaultLocaleIfNecessary(locale);

		String cacheKey = (I18N_KEY + locale.toString()) + "_" + bundleName;

		@SuppressWarnings("unchecked")
		Map<String, String> i18n = (Map<String, String>) cacheManager
				.getObject(Map.class, cacheKey);

		if (i18n == null) {
			i18n = new HashMap<String, String>();

			if (locale != null) {
				ResourceBundle rBundle = this.getBundle(bundleName, locale);

				if (rBundle != null) {
					Enumeration<String> keys = rBundle.getKeys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						LoggerFactory.getLogger(
								AbstractTranslator.class.getName()).info(
								"-> Key: {0}, Message: {1}",
								new Object[] { key, rBundle.getString(key) });
						i18n.put(key, rBundle.getString(key));
					}
					int cacheTime = getCacheTime();
					System.out
							.println("storing i18n map in chache with cacheKey: "
									+ cacheKey + ", cacheTime: " + cacheTime);

					cacheManager.add(cacheKey, i18n, cacheTime);
				}
			}
		}
		return i18n;
	}

	@Override
	public Locale getDefaultLocale() {
		Locale defaultLocale = application.getDefaulLocale();
		if (defaultLocale == null) {
			defaultLocale = Locale.getDefault();
		}
		return defaultLocale;
	}

	@Override
	public List<Locale> getSupportedLocales() {
		@SuppressWarnings("unchecked")
		List<Locale> supportedLocales = cacheManager.getObject(List.class,
				SUPORTED_LOCALES_KEY);

		if (supportedLocales == null) {
			supportedLocales = new ArrayList<Locale>();

			LoggerFactory.getLogger(AbstractTranslator.class.getName()).info(
					"loading supportedLocales...");

			for (Locale locale : application.getSupportedLocales()) {
				LoggerFactory.getLogger(AbstractTranslator.class.getName())
						.info("--> {0}", locale.getDisplayName());
				supportedLocales.add(locale);
			}

			cacheManager.add(SUPORTED_LOCALES_KEY, supportedLocales,
					getCacheTime());
		}
		return supportedLocales;
	}

}
