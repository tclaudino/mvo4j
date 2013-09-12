package br.com.cd.scaleframework.context;

import java.util.Locale;

public interface Translator {

	String getMessage(String messageKey);

	String getMessage(String messageKey, Locale locale);

	String getMessage(String messagePrefix, String messageKey);

	String getMessage(String messagePrefix, String messageKey, Locale locale);

	String getMessage(String messageKey, Object... args);

	String getMessage(String messageKey, Locale locale, Object... args);

	String getMessage(String messagePrefix, String messageKey, Object... args);

	String getMessage(String messagePrefix, String messageKey, Locale locale,
			Object... args);

	String getBundleName();

	void setBundleName(String bundleName);

	Locale getCurrentLocale();

	void setCurrentLocale(Locale locale);
}