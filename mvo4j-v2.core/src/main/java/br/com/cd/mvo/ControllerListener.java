package br.com.cd.mvo;

import br.com.cd.mvo.core.BeanObjectListener;

public interface ControllerListener<T> extends BeanObjectListener<Controller<T>> {

	boolean beforePersist(PersistEventType event, T entity, Application application);

	void postPersist(PersistEventType event, T entity, Application application);

	void onPersistError(PersistEventType event, T entity, Application application, Throwable t);

}