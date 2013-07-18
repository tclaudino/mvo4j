package br.com.cd.scaleframework.core;

public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 937770001077864529L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}