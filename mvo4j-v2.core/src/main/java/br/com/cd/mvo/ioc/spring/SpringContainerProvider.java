package br.com.cd.mvo.ioc.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.ioc.LocalPropertyContainerConfig;

public class SpringContainerProvider implements ContainerProvider<LocalPropertyContainerConfig> {

	@Override
	public Container getContainer(LocalPropertyContainerConfig config) throws ConfigurationException {

		ConfigurableApplicationContext applicationContext = getApplicationContext(config);

		config.setListener(new SpringContainerListener(applicationContext));

		Container container = new SpringContainer(applicationContext, config);

		return container;
	}

	private ConfigurableApplicationContext getApplicationContext(LocalPropertyContainerConfig contextConfig) {

		ConfigurableApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();

		if (applicationContext != null)
			return new GenericApplicationContext(applicationContext);

		if (SpringContainerProvider.class.getResource("/applicationContext.xml") != null) {
			// logger.info("Using an XmlWebApplicationContext, searching for applicationContext.xml");
			GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");
			return ctx;
		}
		// logger.info("No application context found");
		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.setId("MVO");
		return ctx;

	}
}
