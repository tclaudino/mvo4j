package br.com.cd.scaleframework.web;

import javax.servlet.ServletContext;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryProvider;
import br.com.cd.scaleframework.beans.factory.ioc.spring.SpringComponentFactoryProvider;
import br.com.cd.scaleframework.core.ConfigurationException;

public class ComponentConfig {

	ServletContext servletContext;

	public ComponentConfig(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

	public ComponentFactoryProvider getProvider() throws ConfigurationException {

		String className = servletContext.getInitParameter("providerClass");

		Class<? extends ComponentFactoryProvider> providerType = (className != null && className
				.isEmpty()) ? getProvider(className) : this
				.getDefaultProvider();

		ComponentFactoryProvider provider = tryInstance(providerType);

		return provider;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ComponentFactoryProvider> getProvider(
			String className) throws ConfigurationException {

		try {
			return (Class<? extends ComponentFactoryProvider>) Class
					.forName(className);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	private Class<? extends ComponentFactoryProvider> getDefaultProvider() {

		return SpringComponentFactoryProvider.class;
	}

	public ComponentFactoryProvider tryInstance(
			Class<? extends ComponentFactoryProvider> providerType)
			throws ConfigurationException {

		ComponentFactoryProvider provider;
		try {
			return providerType.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}
}
