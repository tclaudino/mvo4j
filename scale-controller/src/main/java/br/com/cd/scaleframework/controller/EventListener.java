package br.com.cd.scaleframework.controller;

import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;

public interface EventListener<T> {

	boolean before(EventType event, T entity, Messenger messenger,
			Translator translator);

	void post(EventType event, T entity, Messenger messenger,
			Translator translator);

	void error(EventType event, T entity, Messenger messenger,
			Translator translator, Throwable t);

	void postConstruct(@SuppressWarnings("rawtypes") Controller controler);

	void preDestroy(@SuppressWarnings("rawtypes") Controller controler);

}