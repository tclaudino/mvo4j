package br.com.cd.scaleframework.context.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.LoggerFactory;

import br.com.cd.scaleframework.context.Application;

public class DefaultApplication extends AbstractApplication {

	private List<Locale> suportedLocales;

	public DefaultApplication(ServletContext servletContext,
			String defaultLocale, String... suportedLocaleLanguages) {
		super(servletContext);

		this.setSuportedLocales(suportedLocaleLanguages);
	}

	@Override
	public List<Locale> getSupportedLocales() {

		return this.suportedLocales;
	}

	private void setSuportedLocales(String... languages) {

		if (this.suportedLocales == null) {
			this.suportedLocales = new ArrayList<Locale>();

			for (String language : languages) {
				LoggerFactory.getLogger(Application.class).info(
						"registering suported locale to language '{0}'",
						language);

				try {
					this.suportedLocales.add(new Locale(language));
				} catch (Exception e) {
					LoggerFactory
							.getLogger(Application.class)
							.error("Error to registry suported locale to language '{0}'",
									language, e);
				}
			}

		}
	}

	@Override
	public Locale getDefaulLocale() {
		return Locale.getDefault();
	}

	@Override
	public String getMessageBundle() {
		return "messages";
	}
}
