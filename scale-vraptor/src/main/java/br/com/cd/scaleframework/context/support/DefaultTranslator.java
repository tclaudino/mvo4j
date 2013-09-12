package br.com.cd.scaleframework.context.support;

import java.util.Locale;

import br.com.cd.scaleframework.context.ApplicationKeys;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;

public class DefaultTranslator extends AbstractTranslator {

	private String bundleName;

	private Locale currentLocale;

	public DefaultTranslator(String bundleName,
			KeyValuesProvider keyValuesProvider) {
		this(bundleName, keyValuesProvider, (Translator) null);
	}

	public DefaultTranslator(String bundleName,
			KeyValuesProvider keyValuesProvider, Translator parent) {
		super(keyValuesProvider, parent);
		this.bundleName = bundleName;
	}

	public DefaultTranslator(String bundleName,
			KeyValuesProvider keyValuesProvider, Locale currentLocale) {
		this(bundleName, keyValuesProvider, (Translator) null);
		this.currentLocale = currentLocale;
	}

	public DefaultTranslator(String bundleName,
			KeyValuesProvider keyValuesProvider, Locale currentLocale,
			Translator parent) {
		this(bundleName, keyValuesProvider, parent);
		this.currentLocale = currentLocale;
	}

	@Override
	public String getBundleName() {
		return this.bundleName;
	}

	@Override
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	@Override
	public Locale getCurrentLocale() {
		if (this.currentLocale == null)
			this.currentLocale = this.keyValuesProvider.getDefaultLocale();

		return this.currentLocale;
	}

	@Override
	public void setCurrentLocale(Locale locale) {
		this.currentLocale = locale;
	}

	private ApplicationKeys keys = new ApplicationKeys();

	public ApplicationKeys getKeys() {
		return keys;
	}

}