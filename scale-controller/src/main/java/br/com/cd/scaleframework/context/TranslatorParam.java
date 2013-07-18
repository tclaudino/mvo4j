package br.com.cd.scaleframework.context;

public class TranslatorParam {

	private final String key;
	private final Object[] parameters;

	public TranslatorParam(String key, Object... parameters) {
		this.key = key;
		this.parameters = parameters;
	}

	public String getKey() {
		return key;
	}

	public Object[] getParameters() {
		return parameters;
	}

}