package br.com.cd.scaleframework.context.suport;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.ExternalApplication;
import br.com.cd.scaleframework.context.InitParamKeys;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.TranslatorKeys;
import br.com.cd.scaleframework.context.TranslatorParam;
import br.com.cd.scaleframework.ioc.BeanFactoryFacadeAware;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;

public abstract class AbstractTranslator implements Translator,
		BeanFactoryFacadeAware<ApplicationContext> {

	public static final String BUNDLE_KEY = AbstractTranslator.class.getName()
			+ "_BUNDLE_KEY_";
	public static final String I18N_KEY = AbstractTranslator.class.getName()
			+ "_I18N_KEY_";
	public static final String SUPORTED_LOCALES_KEY = AbstractTranslator.class
			.getName() + "_SUPORTED_LOCALES_KEY_";

	private Locale currentLocale;

	private CacheManager cacheManager;

	private ExternalApplication application;

	@Override
	public void init(ApplicationContext applicationContext) {

		this.cacheManager = applicationContext.getBean(CacheManager.class);
		this.application = applicationContext
				.getBean(ExternalApplication.class);
	}

	private int getCacheTime() {
		return application.getParameter(InitParamKeys.TRANSLATES_CACHETIME,
				InitParamKeys.DefaultValues.TRANSLATES_CACHETIME);
	}

	@Override
	public String getMessage(String messageKey) {
		return getMessage("", messageKey);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey) {
		return getMessage(messageKey, messageKey, getCurrentLocale());
	}

	@Override
	public String getMessage(String messageKey, Locale locale) {
		Map<String, String> i18n = getI18n(locale);
		if (i18n != null) {
			if (!"".equals(messageKey)) {
				if (i18n.containsKey(messageKey)) {
					return i18n.get(messageKey);
				}
			}
		}
		return messageKey;
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Locale locale) {
		messageKey = ParserUtils.parseString(messageKey);
		messagePrefix = ParserUtils.parseString(messagePrefix);
		if (!messagePrefix.isEmpty()) {
			messageKey = messagePrefix + ("." + messageKey);
		}
		return getMessage(messageKey, locale);
	}

	@Override
	public String getMessage(String messageKey, Object... args) {
		return getMessage("", messageKey, args);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Object... args) {
		return getMessage(messagePrefix, messageKey, getCurrentLocale(), args);
	}

	@Override
	public String getMessage(String messageKey, Locale locale, Object... args) {
		return getMessage("", messageKey, locale, args);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Locale locale, Object... args) {
		return StringUtils.format(
				getMessage(messagePrefix, messageKey, locale), i18n(args));
	}

	private Object[] i18n(Object... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] instanceof TranslatorParam) {
				TranslatorParam tp = (TranslatorParam) parameters[i];
				parameters[i] = getMessage(tp.getKey(), tp.getParameters());
			}
		}
		return parameters;
	}

	private TranslatorKeys keys = new TranslatorKeys();

	public TranslatorKeys getKeys() {
		return keys;
	}

	public static String getMessage(Translator translator, String messageKey,
			Object... args) {
		return translator != null ? translator.getMessage(messageKey, args)
				: messageKey;
	}

	public List<Locale> getSupportedLocales() {
		@SuppressWarnings("unchecked")
		List<Locale> supportedLocales = cacheManager.getObject(List.class,
				SUPORTED_LOCALES_KEY);

		if (supportedLocales == null) {
			supportedLocales = new ArrayList<Locale>();

			Logger.getLogger(AbstractTranslator.class.getName()).log(
					Level.INFO, "loading supportedLocales...");

			for (Locale locale : application.getSupportedLocales()) {
				Logger.getLogger(AbstractTranslator.class.getName()).log(
						Level.INFO, "--> {0}", locale.getDisplayName());
				supportedLocales.add(locale);
			}

			cacheManager.add(SUPORTED_LOCALES_KEY, supportedLocales,
					getCacheTime());
		}
		return supportedLocales;
	}

	private ResourceBundle getBundle(Locale locale) {
		locale = getDefaultLocale(locale);

		String cacheKey = (BUNDLE_KEY + locale.toString() + "_" + getBundleName());

		ResourceBundle bundle = cacheManager.getObject(ResourceBundle.class,
				cacheKey);
		if (bundle == null) {
			String bundleName = getBundleName();

			try {
				Logger.getLogger(AbstractTranslator.class.getName()).log(
						Level.INFO, "\nloading Bundle: {0}, Locale: {1}",
						new Object[] { bundleName, locale });

				bundle = ResourceBundle.getBundle(bundleName, locale);

				int cacheTime = getCacheTime();
				System.out.println("storing bundle in chache with cacheKey: "
						+ cacheKey + ", cacheTime: " + cacheTime);

				cacheManager.add(cacheKey, bundle, cacheTime);
			} catch (Exception e) {
				Logger.getLogger(AbstractTranslator.class.getName()).log(
						Level.SEVERE,
						"\ncoud not load bundle: {0}, locale: {1}",
						new Object[] { bundleName, getCurrentLocale() });
			}
		}
		return bundle;
	}

	public Map<String, String> getI18n() {
		return getI18n(getCurrentLocale());
	}

	public Map<String, String> getI18n(Locale locale) {
		locale = getDefaultLocale(locale);

		String cacheKey = (I18N_KEY + locale.toString()) + "_"
				+ getBundleName();

		@SuppressWarnings("unchecked")
		Map<String, String> i18n = (Map<String, String>) cacheManager
				.getObject(Map.class, cacheKey);

		if (i18n == null) {
			i18n = new HashMap<String, String>();

			if (locale != null) {
				ResourceBundle rBundle = getBundle(locale);

				if (rBundle != null) {
					Enumeration<String> keys = rBundle.getKeys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						Logger.getLogger(AbstractTranslator.class.getName())
								.log(Level.INFO,
										"-> Key: {0}, Message: {1}",
										new Object[] { key,
												rBundle.getString(key) });
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
	public Locale getCurrentLocale() {
		return getDefaultLocale(currentLocale);
	}

	@Override
	public void setCurrentLocale(Locale locale) {
		this.currentLocale = locale;
	}

	@Override
	public Locale getDefaultLocale(Locale defaultLocale) {
		if (defaultLocale == null) {

			defaultLocale = getDefaultLocale();
		}
		return defaultLocale;
	}

	@Override
	public Locale getDefaultLocale() {
		Locale defaultLocale = application.getDefaulLocale();
		if (defaultLocale == null) {
			defaultLocale = Locale.getDefault();
		}
		return defaultLocale;
	}

	private String bundleName;

	@Override
	public String getBundleName() {
		if (bundleName == null) {
			bundleName = application.getMessageBundle();
		}
		return bundleName;
	}

	@Override
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

}
