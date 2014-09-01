package br.com.cd.mvo.web.ioc.spring;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.ioc.spring.SpringContainer;
import br.com.cd.mvo.web.ioc.WebContainerConfig;

public class SpringWebContainerProvider implements ContainerProvider<WebContainerConfig> {

	@Override
	public Container getContainer(WebContainerConfig config) throws ConfigurationException {

		ConfigurableWebApplicationContext applicationContext = getApplicationContext(config);

		config.setListener(new SpringWebContainerListener(applicationContext));

		Container container = new SpringContainer(applicationContext, config);

		return container;
	}

	private ConfigurableWebApplicationContext getApplicationContext(WebContainerConfig config) {

		ConfigurableWebApplicationContext applicationContext = (ConfigurableWebApplicationContext) WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getLocalContainer());
		applicationContext.setServletContext(config.getLocalContainer());

		return applicationContext;
	}
}
