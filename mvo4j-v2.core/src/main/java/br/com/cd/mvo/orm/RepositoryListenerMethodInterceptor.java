package br.com.cd.mvo.orm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.com.cd.mvo.ioc.Container;

public class RepositoryListenerMethodInterceptor<T> extends AbstractListenerMethodInterceptor<T, Repository<T, ?>, RepositoryListener<T>> {

	public RepositoryListenerMethodInterceptor(Repository<T, ?> bean, Container container) {
		super(bean, container);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		switch (method.getName()) {
		case "save":
			return beforeSave((T) args[0], invokeCallback);
		case "update":
			return beforeUpdate((T) args[0], invokeCallback);
		case "delete":
			return beforeDelete((T) args[0], invokeCallback);
		default:
			return super.beforeInvoke(target, method, invokeCallback, args);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {
		switch (method.getName()) {
		case "save":
			afterSave((T) result);
			break;
		case "update":
			afterUpdate((T) result);
			break;
		case "delete":
			afterDelete((T) result);
			break;
		case "find":
			onfind((T) result);
			break;
		case "findByQuery":
			onfind((T) result);
			break;
		case "findByNamedQuery":
			onfind((T) result);
			break;
		case "findList":
			onFindList((List<T>) result);
			break;
		case "findListByQuery":
			onFindList((List<T>) result);
			break;
		case "findListByNamedQuery":
			onFindList((List<T>) result);
			break;
		default:
			super.afterInvoke(target, method, args);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private T beforeSave(T entity, InvokeCallback invokeCallback) throws Throwable {
		boolean save = true;
		for (RepositoryListener<T> listener : this.listeners) {
			if (listener.onSave(entity, RepositoryListener.ActionListenerEventType.BEFORE)) {
				save = true;
			}
		}
		if (save)
			entity = (T) invokeCallback.invoke();

		return entity;
	}

	private void afterSave(T entity) {
		for (RepositoryListener<T> listener : this.listeners) {
			listener.onSave(entity, RepositoryListener.ActionListenerEventType.AFTER);
		}
	}

	@SuppressWarnings("unchecked")
	private T beforeUpdate(T entity, InvokeCallback invokeCallback) throws Throwable {

		boolean save = true;
		for (RepositoryListener<T> listener : this.listeners) {
			if (listener.onUpdate(entity, RepositoryListener.ActionListenerEventType.BEFORE)) {
				save = true;
			}
		}
		if (save)
			entity = (T) invokeCallback.invoke();

		return entity;
	}

	private void afterUpdate(T entity) {
		for (RepositoryListener<T> listener : this.listeners) {
			listener.onUpdate(entity, RepositoryListener.ActionListenerEventType.AFTER);
		}
	}

	private T beforeDelete(final T entity, InvokeCallback invokeCallback) throws Throwable {
		boolean save = true;
		for (RepositoryListener<T> listener : this.listeners) {
			if (listener.onDelete(entity, RepositoryListener.ActionListenerEventType.BEFORE)) {
				save = true;
			}
		}
		if (save)
			invokeCallback.invoke();

		return entity;
	}

	private void afterDelete(T entity) {
		for (RepositoryListener<T> listener : this.listeners) {
			listener.onDelete(entity, RepositoryListener.ActionListenerEventType.AFTER);
		}
	}

	private void onfind(T toResult) {
		if (toResult != null) {
			List<T> list = new ArrayList<>();
			list.add(toResult);
			for (RepositoryListener<T> listener : this.listeners) {
				listener.onRead(list);
			}
		}
	}

	private void onFindList(List<T> listResult) {
		if (listResult != null) {
			for (RepositoryListener<T> listener : this.listeners) {
				listener.onRead(listResult);
			}
		}
	}
}
