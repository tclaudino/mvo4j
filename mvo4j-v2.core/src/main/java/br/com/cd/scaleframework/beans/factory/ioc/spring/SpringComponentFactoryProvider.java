package br.com.cd.scaleframework.beans.factory.ioc.spring;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryProvider;

public class SpringComponentFactoryProvider implements
		ComponentFactoryProvider {

	@Override
	public ComponentFactoryContainer getContainer(
			ServletContext servletContext) {

		ComponentFactoryContainer context = new SpringComponentFactoryContainer(
				getParentApplicationContext(servletContext), servletContext);

		return context;
	}

	private ConfigurableWebApplicationContext getParentApplicationContext(
			ServletContext context) {
		return (ConfigurableWebApplicationContext) WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
	}
}
