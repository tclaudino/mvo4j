package br.com.cd.mvo.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.web.ioc.WebApplicationConfig;

public class ContextLoaderListener implements ServletContextListener {

	ServletContext servletContext;
	Container container;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		container.stop();
		container = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		this.servletContext = contextEvent.getServletContext();
		try {
			ContainerConfig<ServletContext> applicationConfig = new WebApplicationConfig(
					servletContext);

			ContainerProvider<ContainerConfig<ServletContext>> provider = applicationConfig
					.getContainerProvider();

			container = provider.getContainer(applicationConfig);
			container.start();

		} catch (ConfigurationException e) {
			throw new RuntimeException(new ServletException(e));
		}

	}
}
