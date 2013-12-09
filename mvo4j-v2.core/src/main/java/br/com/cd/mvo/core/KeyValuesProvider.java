package br.com.cd.mvo;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface KeyValuesProvider {

	public String BEAN_NAME = KeyValuesProvider.class.getName();

	Map<String, String> getKeyValues(String bundleName);

	Map<String, String> getKeyValues(String bundleName, Locale locale);

	Locale getDefaultLocale();

	List<Locale> getSupportedLocales();
}
