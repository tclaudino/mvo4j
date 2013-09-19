package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.support.DefaultTranslator;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class TranslatorDynamicBeanFactory extends
		AbstractDynamicBeanFactory<Translator> {

	private KeyValuesProvider keyValuesProvider;
	private String defaultLocale;
	private String bundleName;
	private String[] suportedLocaleLanguages;

	public TranslatorDynamicBeanFactory(ComponentFactoryContainer container) {
		super(container);
	}

	@Override
	public Class<Translator> getObjectType() {
		return Translator.class;
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
			defaultLocale = getContainer().getDiscoveryFactory()
					.getDefaultLocale();

		return defaultLocale;
	}

	private String getBundleName() throws ConfigurationException {
		if (bundleName == null)
			bundleName = getContainer().getDiscoveryFactory().getBundleName();

		return bundleName;
	}

	private String[] getSuportedLocaleLanguages() throws ConfigurationException {
		if (suportedLocaleLanguages == null)
			suportedLocaleLanguages = getContainer().getDiscoveryFactory()
					.getSuportedLocales();

		return suportedLocaleLanguages;
	}

	private KeyValuesProvider getKeyValuesProvider()
			throws NoSuchBeanDefinitionException {

		if (keyValuesProvider == null)
			keyValuesProvider = getContainer().getBean(KeyValuesProvider.class);

		return keyValuesProvider;
	}
}
