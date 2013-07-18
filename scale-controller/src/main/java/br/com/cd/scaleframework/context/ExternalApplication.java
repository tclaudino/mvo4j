package br.com.cd.scaleframework.context;

import java.util.List;
import java.util.Locale;

public interface ExternalApplication {

	List<Locale> getSupportedLocales();

	Locale getDefaulLocale();

	String getMessageBundle();

	Object getParameter(String parameterName);

	<T> T getParameter(String parameterName, T defaultValue);
}