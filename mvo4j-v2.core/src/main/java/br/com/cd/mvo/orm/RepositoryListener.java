package br.com.cd.mvo.orm;

import java.util.List;

public interface RepositoryListener<T> {

	public enum ActionListenerEventType {

		BEFORE, AFTER;
	}

	void postConstruct(Repository<T> repository);

	void preDestroy(Repository<T> repository);

	void onRead(final List<T> entity);

	boolean onSave(final T entity, ActionListenerEventType eventType);

	boolean onUpdate(final T entity, ActionListenerEventType eventType);

	boolean onDelete(final T entity, ActionListenerEventType eventType);

}