package br.com.cd.scaleframework.context.support;

import java.util.Locale;
import java.util.Map;

import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.TranslatorParam;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;

public abstract class AbstractTranslator implements Translator {

	protected KeyValuesProvider keyValuesProvider;
	private Translator parent;

	public AbstractTranslator(KeyValuesProvider keyValuesProvider,
			Translator parent) {
		this.keyValuesProvider = keyValuesProvider;
		this.parent = parent;
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

}
