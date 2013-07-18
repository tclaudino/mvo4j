package br.com.cd.scaleframework.web.context;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.context.TranslatorFacade;

@Component
@Scope("session")
public class WebTranslator extends TranslatorFacade {

	private Locale currentLocale;

	@Override
	public Locale getCurrentLocale() {
		return getDefaultLocale(currentLocale);
	}

	@Override
	public void setCurrentLocale(Locale locale) {
		this.currentLocale = locale;
	}

}