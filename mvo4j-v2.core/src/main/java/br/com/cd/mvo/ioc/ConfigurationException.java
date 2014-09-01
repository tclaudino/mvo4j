package br.com.cd.mvo.ioc;

public class ConfigurationException extends Exception {

	public ConfigurationException(Exception e) {
		super(e);
	}

	public ConfigurationException(String message) {
		super(message);
	}

}
