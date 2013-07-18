package br.com.cd.scaleframework.context;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class DefaultSpringLocator {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultSpringLocator.class);

	public ConfigurableWebApplicationContext getApplicationContext(
			ServletContext servletContext) {
		ConfigurableWebApplicationContext context = (ConfigurableWebApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		if (context != null) {
			logger.info("Using a web application context: " + context);
			return context;
		}
		if (DefaultSpringLocator.class.getResource("/applicationContext.xml") != null) {
			logger.info("Using an XmlWebApplicationContext, searching for applicationContext.xml");
			XmlWebApplicationContext ctx = new XmlWebApplicationContext();
			ctx.setConfigLocation("classpath:applicationContext.xml");
			return ctx;
		}
		logger.info("No application context found");
		ConfigurableWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.setId("JModules");
		return ctx;
	}

}
