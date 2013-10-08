package br.com.cd.mvo.core;

import br.com.cd.mvo.Application;

public interface ControllerListener<T> {

	boolean beforePersist(PersistEventType event, T entity,
			Application application);

	void postPersist(PersistEventType event, T entity, Application application);

	void onPersistError(PersistEventType event, T entity,
			Application application, Throwable t);

	void postConstruct(Controller<T> controler);

	void preDestroy(Controller<T> controler);

}