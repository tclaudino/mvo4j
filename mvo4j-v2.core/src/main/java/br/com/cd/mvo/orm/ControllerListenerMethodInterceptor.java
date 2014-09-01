package br.com.cd.mvo.orm;

import java.lang.reflect.Method;

import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.ApplicationKeys;
import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ControllerListener;
import br.com.cd.mvo.CrudController;
import br.com.cd.mvo.PersistEventType;
import br.com.cd.mvo.core.DataModel;
import br.com.cd.mvo.ioc.Container;

public class ControllerListenerMethodInterceptor<T> extends AbstractListenerMethodInterceptor<T, Controller<T>, ControllerListener<T>> {

	private CrudController<T> bean;

	public ControllerListenerMethodInterceptor(CrudController<T> bean, Container container) {
		super(bean, container);
		this.bean = bean;
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
		case "toNewMode":
			return beforeToNewMode(invokeCallback);
		case "toEditMode":
			return beforeToEditMode(invokeCallback);
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
		default:
			super.afterInvoke(target, method, args);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private T beforeSave(T entity, InvokeCallback invokeCallback) throws Throwable {
		boolean canSave = true;
		for (ControllerListener<T> listener : this.listeners) {
			if (listener.beforePersist(PersistEventType.NEW, entity, this.bean.getApplication()))
				canSave = true;
		}
		if (canSave) {
			try {
				entity = (T) invokeCallback.invoke();
			} catch (Exception e) {
				for (ControllerListener<T> listener : this.listeners) {
					listener.onPersistError(PersistEventType.NEW, entity, this.bean.getApplication(), e);
				}
			}
		}
		return entity;
	}

	private void afterSave(T entity) {
		for (ControllerListener<T> listener : this.listeners) {
			listener.postPersist(PersistEventType.NEW, entity, this.bean.getApplication());
		}
	}

	@SuppressWarnings("unchecked")
	private T beforeUpdate(T entity, InvokeCallback invokeCallback) throws Throwable {

		boolean canSave = true;
		for (ControllerListener<T> listener : this.listeners) {
			if (listener.beforePersist(PersistEventType.UPDATE, entity, this.bean.getApplication()))
				canSave = true;
		}
		if (canSave) {
			try {
				entity = (T) invokeCallback.invoke();
			} catch (Exception e) {
				for (ControllerListener<T> listener : this.listeners) {
					listener.onPersistError(PersistEventType.UPDATE, entity, this.bean.getApplication(), e);
				}
			}
		}
		return entity;
	}

	private void afterUpdate(T entity) {
		for (ControllerListener<T> listener : this.listeners) {
			listener.postPersist(PersistEventType.UPDATE, entity, this.bean.getApplication());
		}
	}

	private T beforeDelete(final T entity, InvokeCallback invokeCallback) throws Throwable {
		boolean canSave = true;
		for (ControllerListener<T> listener : this.listeners) {
			if (listener.beforePersist(PersistEventType.DELETE, entity, this.bean.getApplication()))
				canSave = true;
		}
		if (canSave) {
			try {
				invokeCallback.invoke();
			} catch (Exception e) {
				for (ControllerListener<T> listener : this.listeners) {
					listener.onPersistError(PersistEventType.DELETE, entity, this.bean.getApplication(), e);
				}
			}
		}
		return entity;
	}

	private void afterDelete(T entity) {
		for (ControllerListener<T> listener : this.listeners) {
			listener.postPersist(PersistEventType.DELETE, entity, this.bean.getApplication());
		}
	}

	private Object beforeToEditMode(InvokeCallback invokeCallback) throws Throwable {

		boolean canEdit = true;

		T entity = this.bean.getCurrentEntity();
		if (this.bean.isListMode()) {

			DataModel<T> entList = this.bean.getDataModel();
			if (entList.isRowAvailable() && entList.getRowData() != null) {
				entity = entList.getRowData();
			}
		}

		for (ControllerListener<T> listener : this.listeners) {
			canEdit = listener.beforePersist(PersistEventType.UPDATE, entity, this.bean.getApplication());
			if (!canEdit)
				break;
		}
		if (canEdit) {
			return invokeCallback.invoke();
		} else {
			this.bean.addTranslatedMessage(MessageLevel.WARNING,
					this.bean.getTranslator().getMessage(ApplicationKeys.Publisher.AcccessDenied.SUMARY), this.bean.getTranslator()
							.getMessage(ApplicationKeys.Publisher.AcccessDenied.UPDATE));
		}
		return null;
	}

	private Object beforeToNewMode(InvokeCallback invokeCallback) throws Throwable {

		boolean canInsert = true;
		for (ControllerListener<T> listener : this.listeners) {
			canInsert = listener.beforePersist(PersistEventType.NEW, this.bean.newEntity(), this.bean.getApplication());
			if (!canInsert)
				break;
		}
		if (canInsert) {
			return invokeCallback.invoke();
		} else {
			this.bean.addTranslatedMessage(MessageLevel.WARNING,
					this.bean.getTranslator().getMessage(ApplicationKeys.Publisher.AcccessDenied.SUMARY), this.bean.getTranslator()
							.getMessage(ApplicationKeys.Publisher.AcccessDenied.INSERT));
		}
		return null;
	}
}
