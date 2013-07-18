package br.com.cd.scaleframework.web.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.context.ExternalApplication;
import br.com.cd.scaleframework.ioc.BeanFactoryFacadeAware;
import br.com.cd.scaleframework.web.util.WebUtil;

@Component
public class WebApplication implements ExternalApplication,
		BeanFactoryFacadeAware<WebApplicationContext> {

	List<Locale> locales;

	private WebApplicationContext applicationContext;

	@Override
	public void init(WebApplicationContext applicationContext) {

		this.applicationContext = applicationContext;
	}

	@Override
	public List<Locale> getSupportedLocales() {

		if (locales == null) {
			locales = new ArrayList<Locale>();

			System.out
					.println("find suported locales from messages_*.properties files");

			Reflections reflections = new Reflections(
					new ConfigurationBuilder()
							.filterInputsBy(
									new FilterBuilder()
											.include("messages.*\\.properties"))
							.addUrls(
									ClasspathHelper
											.forWebInfClasses(applicationContext
													.getServletContext()))
							.setScanners(new ResourcesScanner()));
			List<String> resourceNames = new ArrayList<String>(reflections
					.getStore().get(ResourcesScanner.class).keySet());

			System.out.println("found properties files: " + resourceNames);
			for (String resourceName : resourceNames) {
				System.out.println("load locale from properties files: "
						+ resourceName);
				if (resourceName.indexOf("_") > -1) {
					String language = resourceName.substring(
							resourceName.indexOf("_") + 1).replace(
							".properties", "");

					System.out.println("discovery laguage : " + language);
					locales.add(new Locale(language));
				}
			}
		}

		return locales;
	}

	@Override
	public Locale getDefaulLocale() {
		return Locale.getDefault();
	}

	@Override
	public String getMessageBundle() {
		return "messages";
	}

	@Override
	public String getParameter(String parameterName) {
		return WebUtil.getContextParameter(
				applicationContext.getServletContext(), parameterName);
	}

	@Override
	public <T> T getParameter(String parameterName, T defaultValue) {
		return WebUtil.getContextParameter(
				applicationContext.getServletContext(), parameterName,
				defaultValue);
	}

}
