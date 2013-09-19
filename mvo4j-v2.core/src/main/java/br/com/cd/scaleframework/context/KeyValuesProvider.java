package br.com.cd.scaleframework.context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface KeyValuesProvider {

	Map<String, String> getKeyValues(String bundleName);

	Map<String, String> getKeyValues(String bundleName, Locale locale);

	Locale getDefaultLocale();

	List<Locale> getSupportedLocales();
}
