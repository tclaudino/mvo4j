package br.com.cd.mvo.core;

@SuppressWarnings("rawtypes")
public interface Listenable<L extends BeanObjectListener> {

	Class<? extends L> getListenerType();

	void postConstruct();

	void preDestroy();
}