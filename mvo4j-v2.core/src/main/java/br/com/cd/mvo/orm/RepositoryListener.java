package br.com.cd.mvo.orm;

import java.util.List;

import br.com.cd.mvo.core.BeanObjectListener;

public interface RepositoryListener<T> extends BeanObjectListener<Repository<T>> {

	public enum ActionListenerEventType {

		BEFORE, AFTER;
	}

	void onRead(final List<T> entity);

	boolean onSave(final T entity, ActionListenerEventType eventType);

	boolean onUpdate(final T entity, ActionListenerEventType eventType);

	boolean onDelete(final T entity, ActionListenerEventType eventType);

}