package br.com.cd.scaleframework.core;

public class BeanException extends RuntimeException {

	private static final long serialVersionUID = 4163813007440715831L;

	public BeanException(String message) {
		super(message);
	}

	public BeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanException(Throwable cause) {
		super(cause);
	}
}