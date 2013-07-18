package br.com.cd.scaleframework.core.modules;

@SuppressWarnings("serial")
public class InvalidModuleException extends Exception {

	public InvalidModuleException() {
		super();
	}

	public InvalidModuleException(String message) {
		super(message);
	}

	public InvalidModuleException(Exception e) {
		super(e);
	}

}
