package br.com.cd.scaleframework.context.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cd.scaleframework.context.ApplicationKeys;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.TranslatorParam;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;

public class DefaultTranslator implements Translator {

	protected Logger logger = LoggerFactory.getLogger(Translator.class);

	private String bundleName;

	private Locale currentLocale;
	private List<Locale> suportedLocales;

	protected KeyValuesProvider keyValuesProvider;
	protected Translator parent;

	public DefaultTranslator(String defaultLocale, String bundleName,
			KeyValuesProvider keyValuesProvider,
			String... suportedLocaleLanguages) {
		this.keyValuesProvider = keyValuesProvider;

		this.setSuportedLocales(suportedLocaleLanguages);
		this.setDefaultLocale(defaultLocale);
	}

	public DefaultTranslator(String bundleName,
			KeyValuesProvider keyValuesProvider, Translator parent) {
		this(parent.getCurrentLocale().getLanguage(), bundleName,
				keyValuesProvider);
		this.parent = parent;
		this.suportedLocales = parent.getSupportedLocales();
	}

	private void setDefaultLocale(String language) {
		LoggerFactory.getLogger(Translator.class).info(
				"registering default locale to language '{0}'", language);

		try {
			this.currentLocale = new Locale(language);
		} catch (Exception e) {
			LoggerFactory.getLogger(Translator.class).error(
					"Error to registry suported locale to language '{0}'",
					language, e);
		}
	}

	private void setSuportedLocales(String... languages) {

		if (this.suportedLocales == null) {
			this.suportedLocales = new ArrayList<Locale>();

			for (String language : languages) {
				LoggerFactory.getLogger(Translator.class).info(
						"registering suported locale to language '{0}'",
						language);

				try {
					this.suportedLocales.add(new Locale(language));
				} catch (Exception e) {
					LoggerFactory
							.getLogger(Translator.class)
							.error("Error to registry suported locale to language '{0}'",
									language, e);
				}
			}
		}
	}

	@Override
	public String getBundleName() {
		return this.bundleName;
	}

	@Override
	public Locale getCurrentLocale() {
		if (this.currentLocale == null)
			if (parent != null)
				this.currentLocale = parent.getCurrentLocale();
			else
				this.currentLocale = this.keyValuesProvider.getDefaultLocale();

		return this.currentLocale;
	}

	@Override
	public void setCurrentLocale(Locale locale) {
		this.currentLocale = locale;
	}

	@Override
	public List<Locale> getSupportedLocales() {
		if (this.suportedLocales == null)
			if (parent != null)
				this.suportedLocales = parent.getSupportedLocales();
			else
				this.suportedLocales = this.keyValuesProvider
						.getSupportedLocales();

		return this.suportedLocales;
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
		Map<String, String> i18n = keyValuesProvider.getKeyValues(
				this.getBundleName(), locale);
		if (i18n != null) {
			if (!"".equals(messageKey)) {
				if (i18n.containsKey(messageKey)) {
					return i18n.get(messageKey);
				} else if (parent != null) {
					return parent.getMessage(messageKey, locale);
				}
			}
		}

		if (logger.isDebugEnabled())
			return "[" + messageKey + "]";

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

	public static String getMessage(Translator translator, String messageKey,
			Object... args) {
		return translator != null ? translator.getMessage(messageKey, args)
				: messageKey;
	}

	private ApplicationKeys keys = new ApplicationKeys();

	@Override
	public ApplicationKeys getKeys() {
		return keys;
	}
}
