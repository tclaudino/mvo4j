package br.com.cd.mvo.ioc;

public class NoSuchBeanDefinitionException extends RuntimeException {

	public NoSuchBeanDefinitionException(Exception e) {
		super(e);
	}

	public NoSuchBeanDefinitionException(String message) {
		super(message);
	}

}
