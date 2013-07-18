package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.TranslatorKeys;
import br.com.cd.scaleframework.controller.suport.AbstractPager;
import br.com.cd.scaleframework.ioc.BeanFactoryFacadeAware;
import br.com.cd.scaleframework.util.ParserUtils;

public abstract class AbstractController<T, ID extends Serializable> extends
		AbstractPager implements Controller<T, ID>, InitializingBean,
		DisposableBean, BeanFactoryFacadeAware<ApplicationContext> {

	protected enum MessageLevel {

		WARNING, ERROR, INFO;
	}

	private Messenger messenger;

	private Translator translator;

	private DataModelFactory modelFactory;

	private DataModel<T> dataModel;

	private T currentEntity;
	private List<T> entityList;

	@Override
	public void init(ApplicationContext applicationContext) {

		this.messenger = applicationContext.getBean(Messenger.class);
		this.translator = applicationContext.getBean(Translator.class);
		this.modelFactory = applicationContext.getBean(DataModelFactory.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (EventListener<T> listener : this.getListeners()) {
			listener.postConstruct(this);
		}
	}

	@Override
	public final void destroy() {
		for (EventListener<T> listener : this.getListeners()) {
			listener.preDestroy(this);
		}
		entityList = null;
		currentEntity = null;
	}

	private List<EventListener<T>> listeners = new LinkedList<EventListener<T>>();

	protected List<EventListener<T>> getListeners() {
		return listeners;
	}

	@Override
	public final void addListener(EventListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public final void resetPager() {
		setRecordsCount(getService().getListCount());
	}

	private PagerCallback pagerCallback;

	@Override
	public final PagerCallback getPagerCallback() {

		if (pagerCallback == null) {
			final long count = getService().getListCount();

			pagerCallback = new PagerCallback() {

				@Override
				public void onRefresh() {
					AbstractController.this.resetList();
				}

				@Override
				public Translator getTranslator() {
					return AbstractController.this.getTranslator();
				}

				@Override
				public Long getRecordsCount() {
					return count;
				}

				@Override
				public Integer getPageSize() {
					return AbstractController.this.getInitialPageSize();
				}
			};
		}
		return pagerCallback;
	}

	@Override
	public final void resetList() {

		Logger.getLogger(this.getClass().getName())
				.log(Level.INFO, "resetList");

		if (entityList != null) {
			entityList.clear();
		}
		entityList = null;

		resetPager();

		getDataModel().setEntityList(getEntityList());
	}

	@Override
	public final void create() {
		setCurrentEntity(newEntity());
	}

	@Override
	public final void save() {
		T entity = getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}
		save(entity);
	}

	@Override
	public final void save(T entity) {
		boolean canSave = true;
		try {
			for (EventListener<T> listener : this.getListeners()) {
				canSave = listener.before(EventType.INSERT, entity, messenger,
						translator);
			}
			if (!canSave) {
				return;
			}
			getService().save(entity);

			for (EventListener<T> listener : this.getListeners()) {
				listener.post(EventType.INSERT, entity, messenger, translator);
			}
			resetList();

			addSuccessMessage(EventType.INSERT);
		} catch (Exception ex) {
			addErrorMessage(EventType.INSERT, ex);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
			for (EventListener<T> listener : this.getListeners()) {
				listener.error(EventType.INSERT, entity, messenger, translator,
						ex);
			}
		}
	}

	@Override
	public final void update() {
		T entity = getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}
		update(entity);
	}

	@Override
	public final void update(T entity) {
		boolean canSave = true;
		try {
			for (EventListener<T> listener : this.getListeners()) {
				canSave = listener.before(EventType.UPDATE, entity, messenger,
						translator);
			}
			if (!canSave) {
				return;
			}
			getService().update(entity);

			for (EventListener<T> listener : this.getListeners()) {
				listener.post(EventType.UPDATE, entity, messenger, translator);
			}
			resetList();

			addSuccessMessage(EventType.UPDATE);
		} catch (Exception ex) {
			addErrorMessage(EventType.UPDATE, ex);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
			for (EventListener<T> listener : this.getListeners()) {
				listener.error(EventType.UPDATE, entity, messenger, translator,
						ex);
			}
		}
	}

	@Override
	public final void delete() {
		T entity = this.getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}
		delete(entity);
	}

	@Override
	public final void delete(T entity) {
		List<T> list = new ArrayList<T>(1);
		list.add(entity);
		delete(list);
	}

	@Override
	public final void delete(List<T> entityList) {
		boolean canDelete = true;
		for (T entity : entityList) {
			try {
				for (EventListener<T> listener : this.getListeners()) {
					canDelete = listener.before(EventType.DELETE, entity,
							messenger, translator);
				}
				if (!canDelete) {
					return;
				}
				getService().delete(entity);

				for (EventListener<T> listener : this.getListeners()) {
					listener.post(EventType.DELETE, entity, messenger,
							translator);
				}
			} catch (Exception ex) {
				addErrorMessage(EventType.UPDATE, ex);
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
						ex.getMessage(), ex);
				for (EventListener<T> listener : this.getListeners()) {
					listener.error(EventType.UPDATE, entity, messenger,
							translator, ex);
				}
			}
		}
		resetList();

		addSuccessMessage(EventType.UPDATE);
	}

	@Override
	public final T getCurrentEntity() {

		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"currentEntity: {0}", new Object[] { currentEntity });

		return currentEntity;
	}

	@SuppressWarnings("unchecked")
	public final Class<T> getEntityClass() {
		if (currentEntity != null) {
			return (Class<T>) currentEntity.getClass();
		}

		return (Class<T>) newEntity().getClass();
	}

	@Override
	public final void setCurrentEntity(T currentEntity) {
		resetList();
		this.currentEntity = currentEntity;
	}

	@Override
	public final void setCurrentEntity(ID entityId) {

		try {
			this.setCurrentEntity(getService().find(entityId));
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"-> Exception", e);

			this.addMessage(MessageLevel.ERROR,
					TranslatorKeys.Entity.NOT_FOUND_SUMARY,
					TranslatorKeys.Entity.NOT_FOUND_MSG, getName());
		}
	}

	@Override
	public final List<T> getEntityList() {
		if (entityList == null) {

			if (getInitialPageSize() > -1) {
				entityList = getService().findList(getOffset(), getPageSize());
			} else {
				entityList = getService().findList();
			}
		}
		return entityList;
	}

	@Override
	public final void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	public String translateIfNecessary(String name, String translateKey) {
		if (java.util.regex.Pattern.matches("\\{[A-Za-z0-9\\.]+\\}",
				translateKey)) {
			translateKey = translateKey.replaceAll("[\\{\\}]", "");
			name = ParserUtils.assertNotEquals(
					translator.getMessage(translateKey), name, name);
		}
		return name;
	}

	@Override
	public final String getNameAsList() {

		String name = getName();
		name = translateIfNecessary(name, (name + ".list"));

		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"nameAsList: {0}", name);

		return name;
	}

	private void addSuccessMessage(EventType executeType) {

		switch (executeType) {
		case INSERT:
			addMessage(MessageLevel.INFO,
					TranslatorKeys.PersistAction.SAVE_SUCCESS_SUMARY,
					TranslatorKeys.PersistAction.SAVE_SUCCESS_MESSAGE,
					getName());
			break;
		case UPDATE:
			addMessage(MessageLevel.INFO,
					TranslatorKeys.PersistAction.UPDATE_SUCCESS_SUMARY,
					TranslatorKeys.PersistAction.UPDATE_SUCCESS_MESSAGE,
					getName());
			break;
		case DELETE:
			addMessage(MessageLevel.INFO,
					TranslatorKeys.PersistAction.DELETE_SUCCESS_SUMARY,
					TranslatorKeys.PersistAction.DELETE_SUCCESS_MESSAGE,
					getName());
			break;
		}
	}

	private void addErrorMessage(EventType executeType, Exception exception) {

		switch (executeType) {
		case INSERT:
			addMessage(MessageLevel.ERROR,
					TranslatorKeys.PersistAction.SAVE_ERROR_SUMARY,
					TranslatorKeys.PersistAction.SAVE_ERROR_MESSAGE, getName(),
					exception.getMessage());
			break;
		case UPDATE:
			addMessage(MessageLevel.ERROR,
					TranslatorKeys.PersistAction.UPDATE_ERROR_SUMARY,
					TranslatorKeys.PersistAction.UPDATE_ERROR_MESSAGE,
					getName(), exception.getMessage());
			break;
		case DELETE:
			addMessage(MessageLevel.ERROR,
					TranslatorKeys.PersistAction.DELETE_ERROR_SUMARY,
					TranslatorKeys.PersistAction.DELETE_ERROR_MESSAGE,
					getName(), exception.getMessage());
			break;
		}
	}

	@Override
	public final void addMessage(MessageLevel level, String msgSumary,
			String msgDetail, Object... args) {

		switch (level) {
		case WARNING:
			messenger.addWarningMessage(translator.getMessage(msgSumary),
					translator.getMessage(msgDetail, args));
			break;
		case ERROR:
			messenger.addErrorMessage(translator.getMessage(msgSumary),
					translator.getMessage(msgDetail, args));
			break;
		case INFO:
			messenger.addInfoMessage(translator.getMessage(msgSumary),
					translator.getMessage(msgDetail, args));
			break;
		}
	}

	@Override
	public final DataModel<T> getDataModel() {
		if (dataModel == null) {
			dataModel = modelFactory.createDataModel(getEntityList());
		}
		return dataModel;
	}

	@Override
	public final Messenger getMessenger() {
		return messenger;
	}

	@Override
	public final Translator getTranslator() {
		applyBundle(translator);
		return translator;
	}

	protected abstract void applyBundle(Translator translator);
}