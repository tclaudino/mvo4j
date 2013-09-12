package br.com.cd.scaleframework.controller;

import java.io.Serializable;

import br.com.cd.scaleframework.context.Application;

public interface ControllerListener<T> {

	boolean beforePersist(PersistEventType event, T entity,
			Application application);

	void postPersist(PersistEventType event, T entity, Application application);

	void onPersistError(PersistEventType event, T entity,
			Application application, Throwable t);

	void postConstruct(Controller<T, ? extends Serializable> controler);

	void preDestroy(Controller<T, ? extends Serializable> controler);

}