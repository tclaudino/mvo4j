package br.com.cd.mvo.core;

import java.util.Collection;

@SuppressWarnings("rawtypes")
public interface Listenable<T extends BeanObjectListener> {

	Collection<T> getListeners();

	void addListener(T listener);

	void afterPropertiesSet();

	void destroy();

	Class<? extends BeanObjectListener> getListenerType();
}