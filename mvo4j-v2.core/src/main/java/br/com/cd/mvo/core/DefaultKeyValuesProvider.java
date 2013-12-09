package br.com.cd.mvo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import br.com.cd.mvo.util.StringUtils;

public class DefaultKeyValuesProvider implements KeyValuesProvider {

	static Logger looger = Logger.getLogger(KeyValuesProvider.class);

	public static final String BUNDLE_KEY = KeyValuesProvider.class.getName() + "_BUNDLE_KEY_";
	public static final String I18N_KEY = KeyValuesProvider.class.getName() + "_I18N_KEY_";
	public static final String SUPORTED_LOCALES_KEY = KeyValuesProvider.class.getName() + "_SUPORTED_LOCALES_KEY_";

	protected CacheManager cacheManager;

	private Locale defaultLocale;
	private List<Locale> suportedLocales;
	private long cacheTime;

	public DefaultKeyValuesProvider(CacheManager cacheManager, String defaultLocale, String... suportedLocaleLanguages) {
		this(cacheManager, ConfigParamKeys.DefaultValues.I18N_CACHE_TIME, defaultLocale, suportedLocaleLanguages);
	}

	public DefaultKeyValuesProvider(CacheManager cacheManager, long cacheTime, String defaultLocale, String... suportedLocaleLanguages) {
		this.cacheManager = cacheManager;
		this.cacheTime = cacheTime;

		this.setSuportedLocales(suportedLocaleLanguages);
		this.setDefaultLocale(defaultLocale);
	}

	private void setDefaultLocale(String language) {
		looger.info(StringUtils.format("registering default locale to language '{0}'", language));

		try {
			this.defaultLocale = new Locale(language);
		} catch (Exception e) {
			looger.error(StringUtils.format("error to registry suported locale to language '{0}'", language), e);
		}
	}

	private void setSuportedLocales(String... languages) {

		if (this.suportedLocales == null) {
			this.suportedLocales = new ArrayList<Locale>();

			for (String language : languages) {
				looger.info(StringUtils.format("registering suported locale to language '{0}'", language));

				try {
					this.suportedLocales.add(new Locale(language));
				} catch (Exception e) {
					looger.error(StringUtils.format("error to registry suported locale to language '{0}'", language), e);
				}
			}
		}
	}

	protected long getCacheTime() {
		return this.cacheTime;
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
		Map<String, String> i18n = (Map<String, String>) cacheManager.getObject(Map.class, cacheKey);

		if (i18n == null) {
			i18n = new HashMap<String, String>();

			if (locale != null) {
				ResourceBundle rBundle = this.getBundle(bundleName, locale);

				if (rBundle != null) {
					Enumeration<String> keys = rBundle.getKeys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						looger.trace(StringUtils.format("-> Key: {0}, Message: {1}", key, rBundle.getString(key)));
						i18n.put(key, rBundle.getString(key));
					}
					long cacheTime = getCacheTime();
					looger.debug(StringUtils.format("storing i18n map in chache with cacheKey '{0}', cacheTime '{1}'", cacheKey, cacheTime));

					cacheManager.add(cacheKey, i18n, cacheTime);
				}
			}
		}
		return i18n;
	}

	@Override
	public Locale getDefaultLocale() {
		if (defaultLocale == null) {
			defaultLocale = new Locale(ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);
		}

		return this.defaultLocale;
	}

	@Override
	public List<Locale> getSupportedLocales() {
		if (this.suportedLocales == null || this.suportedLocales.isEmpty())
			this.setSuportedLocales(ConfigParamKeys.DefaultValues.SUPORTED_LOCALES.split(","));

		return this.suportedLocales;
	}

	protected ResourceBundle getBundle(String bundleName, Locale locale) {

		String cacheKey = (BUNDLE_KEY + locale.toString() + "_" + bundleName);

		ResourceBundle bundle = cacheManager.getObject(ResourceBundle.class, cacheKey);
		if (bundle == null) {

			try {
				looger.debug(StringUtils.format("loading Bundle '{0}', locale '{1}'", bundleName, locale));

				bundle = ResourceBundle.getBundle(bundleName, locale);

				long cacheTime = this.getCacheTime();
				looger.debug(StringUtils.format("storing bundle in chache with cacheKey '{0}', cacheTime '{1}'", cacheKey, cacheTime));

				cacheManager.add(cacheKey, bundle, cacheTime);
			} catch (Exception e) {
				looger.error(StringUtils.format("coud not load bundle '{0}', locale '{1}'", bundleName, locale), e);
			}
		}
		return bundle;
	}

	protected Locale setDefaultLocaleIfNecessary(Locale locale) {
		if (locale == null) locale = getDefaultLocale();

		return locale;
	}
}
