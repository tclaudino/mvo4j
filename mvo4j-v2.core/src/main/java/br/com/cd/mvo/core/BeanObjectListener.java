package br.com.cd.mvo.core;

public interface BeanObjectListener<T extends BeanObject<?>> {

	public static final String BEAN_NAME_SUFFIX = "Listener";

	void postConstruct(T bean);

	void preDestroy(T bean);

}