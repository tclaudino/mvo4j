package br.com.cd.mvo.ioc;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.DefaultTranslator;
import br.com.cd.mvo.core.KeyValuesProvider;

public class TranslatorComponentFactory extends AbstractComponentFactory<Translator> {

	private KeyValuesProvider keyValuesProvider;
	private String defaultLocale;
	private String bundleName;
	private String[] suportedLocaleLanguages;

	public TranslatorComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected Translator getInstanceInternal() throws NoSuchBeanDefinitionException {

		try {
			return new DefaultTranslator(getDefaultLocale(), getBundleName(), getKeyValuesProvider(), getSuportedLocaleLanguages());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	protected String getComponentBeanName() {
		return Translator.BEAN_NAME;
	}

	private String getDefaultLocale() throws ConfigurationException {
		if (defaultLocale == null)
			defaultLocale = container.getContainerConfig().getInitParameter(ConfigParamKeys.DEFAULT_LOCALE, ConfigParamKeys.DefaultValues.DEFAULT_LOCALE);

		return defaultLocale;
	}

	private String getBundleName() throws ConfigurationException {
		if (bundleName == null)
			bundleName = container.getContainerConfig()
					.getInitParameter(ConfigParamKeys.MESSAGE_BUNDLE_NAME, ConfigParamKeys.DefaultValues.MESSAGE_BUNDLE_NAME);

		return bundleName;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = container.getContainerConfig()
					.getInitParameter(ConfigParamKeys.SUPORTED_LOCALES, ConfigParamKeys.DefaultValues.SUPORTED_LOCALES).split(",");

		return suportedLocaleLanguages;
	}

	private KeyValuesProvider getKeyValuesProvider() throws NoSuchBeanDefinitionException {

		if (keyValuesProvider == null)
			keyValuesProvider = container.getBean(KeyValuesProvider.BEAN_NAME, KeyValuesProvider.class);

		return keyValuesProvider;
	}
}
