package br.com.cd.scaleframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryProvider;
import br.com.cd.scaleframework.core.ConfigurationException;

public class ContextLoaderListener implements ServletContextListener {

	ServletContext servletContext;
	ComponentFactoryContainer context;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		context.stop();
		context = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		this.servletContext = contextEvent.getServletContext();
		ComponentConfig config = new ComponentConfig(this.servletContext);

		ComponentFactoryProvider provider;
		try {
			provider = config.getProvider();
		} catch (ConfigurationException e) {
			throw new RuntimeException(new ServletException(e));
		}

		context = provider.getContainer(this.servletContext);
		context.start();
	}
}
