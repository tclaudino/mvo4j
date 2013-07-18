package br.com.cd.scaleframework.context;

import java.util.Locale;

import br.com.cd.scaleframework.context.suport.AbstractTranslator;

public abstract class TranslatorFacade implements Translator {

	private static Translator translator;

	public TranslatorFacade() {
		if (TranslatorFacade.translator == null) {
			TranslatorFacade.translator = new AbstractTranslator() {
			};
		}
	}

	@Override
	public Locale getDefaultLocale() {
		return translator.getDefaultLocale();
	}

	@Override
	public Locale getDefaultLocale(Locale defaultLocale) {
		return translator.getDefaultLocale(defaultLocale);
	}

	@Override
	public String getMessage(String messageKey) {
		return translator.getMessage(messageKey);
	}

	@Override
	public String getMessage(String messageKey, Locale locale) {
		return translator.getMessage(messageKey, locale);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey) {
		return translator.getMessage(messagePrefix, messageKey);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Locale locale) {
		return translator.getMessage(messagePrefix, messageKey, locale);
	}

	@Override
	public String getMessage(String messageKey, Object... args) {
		return translator.getMessage(messageKey, args);
	}

	@Override
	public String getMessage(String messageKey, Locale locale, Object... args) {
		return translator.getMessage(messageKey, locale, args);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Object... args) {
		return translator.getMessage(messagePrefix, messageKey, args);
	}

	@Override
	public String getMessage(String messagePrefix, String messageKey,
			Locale locale, Object... args) {
		return translator.getMessage(messagePrefix, messageKey, locale, args);
	}

	@Override
	public String getBundleName() {
		return translator.getBundleName();
	}

	@Override
	public void setBundleName(String bundleName) {
		translator.setBundleName(bundleName);
	}
}