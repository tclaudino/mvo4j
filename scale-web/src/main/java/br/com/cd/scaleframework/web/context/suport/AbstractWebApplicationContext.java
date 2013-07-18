package br.com.cd.scaleframework.web.context.suport;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.reflections.Configuration;
import org.reflections.scanners.Scanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import br.com.cd.scaleframework.context.support.AbstractApplicationContext;
import br.com.cd.scaleframework.ioc.BeanFactoryFacade;
import br.com.cd.scaleframework.ioc.BeanRegistryFacade;
import br.com.cd.scaleframework.web.context.WebApplicationContext;

public abstract class AbstractWebApplicationContext extends
		AbstractApplicationContext implements WebApplicationContext {

	private ServletContext servletContext;

	public AbstractWebApplicationContext(BeanRegistryFacade beanRegistry,
			BeanFactoryFacade beanFactory, ServletContext servletContext) {
		super(beanRegistry, beanFactory);
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public Configuration createConfiguration(FilterBuilder filter,
			Scanner... scanners) {

		Set<URL> urls = new HashSet<URL>();

		urls.addAll(ClasspathHelper.forJavaClassPath());
		urls.addAll(ClasspathHelper.forManifest());
		urls.addAll(ClasspathHelper.forWebInfLib(this.servletContext));
		urls.add(ClasspathHelper.forWebInfClasses(this.servletContext));

		return new ConfigurationBuilder().filterInputsBy(filter).setUrls(urls)
				.setScanners(scanners);
	}
}
