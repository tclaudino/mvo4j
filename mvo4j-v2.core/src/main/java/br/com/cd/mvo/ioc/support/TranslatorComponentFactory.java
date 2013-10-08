package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.DefaultTranslator;
import br.com.cd.mvo.KeyValuesProvider;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;

public class TranslatorComponentFactory extends
		AbstractComponentFactory<Translator> {

	private KeyValuesProvider keyValuesProvider;
	private String defaultLocale;
	private String bundleName;
	private String[] suportedLocaleLanguages;

	public TranslatorComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected Translator getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		try {
			return new DefaultTranslator(getDefaultLocale(), getBundleName(),
					getKeyValuesProvider(), getSuportedLocaleLanguages());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	private String getDefaultLocale() throws ConfigurationException {
		if (defaultLocale == null)
			defaultLocale = container.getInitApplicationConfig()
					.getDefaultLocale();

		return defaultLocale;
	}

	private String getBundleName() throws ConfigurationException {
		if (bundleName == null)
			bundleName = container.getInitApplicationConfig().getBundleName();

		return bundleName;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = container.getInitApplicationConfig()
					.getSuportedLocales();

		return suportedLocaleLanguages;
	}

	private KeyValuesProvider getKeyValuesProvider()
			throws NoSuchBeanDefinitionException {

		if (keyValuesProvider == null)
			keyValuesProvider = container.getBean(KeyValuesProvider.class);

		return keyValuesProvider;
	}
}
