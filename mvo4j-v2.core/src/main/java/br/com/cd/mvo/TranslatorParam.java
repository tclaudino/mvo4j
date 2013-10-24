package br.com.cd.mvo;

public class TranslatorParam {

	private final String key;
	private final Object[] parameters;

	public TranslatorParam(String key, Object... parameters) {
		this.key = key;
		this.parameters = parameters;
	}

	public static TranslatorParam build(String key, Object... parameters) {

		return new TranslatorParam(key, parameters);
	}

	public String getKey() {
		return key;
	}

	public Object[] getParameters() {
		return parameters;
	}

}