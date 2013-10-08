package br.com.cd.mvo.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.ApplicationKeys;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.util.ParserUtils;

public class DefaultController<T> extends AbstractPageableController implements
		Controller<T> {

	protected Logger logger = LoggerFactory
			.getLogger(this.getClass().getName());

	protected Application application;

	protected Translator translator;

	private DataModelFactory modelFactory;

	protected ControllerMetaData config;

	public DefaultController(Application application, Translator translator,
			DataModelFactory modelFactory, CrudService<T> service,
			ControllerMetaData config) {

		this.application = application;
		this.translator = translator;
		this.modelFactory = modelFactory;
		this.config = config;
		this.service = service;
	}

	private DataModel<T> dataModel;

	private T currentEntity;
	private List<T> entityList;

	private CrudService<T> service;

	protected FilterManager FilterManager = new DefaultFilterManager<T>(this);

	private List<ControllerListener<T>> listeners = new LinkedList<ControllerListener<T>>();

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		for (ControllerListener<T> listener : this.getListeners()) {
			listener.postConstruct(this);
		}
	}

	@PreDestroy
	public final void destroy() {
		for (ControllerListener<T> listener : this.getListeners()) {
			listener.preDestroy(this);
		}
		entityList = null;
		currentEntity = null;
	}

	@Override
	public CrudService<T> getService() {
		return this.service;
	}

	@Override
	public void setService(CrudService<T> service) {
		this.service = service;
	}

	protected List<ControllerListener<T>> getListeners() {
		return listeners;
	}

	@Override
	public final void addListener(ControllerListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public ControllerMetaData getControllerConfig() {
		return config;
	}

	@Override
	public final void resetPager() {
		this.setRecordsCount(getService().getRepository().getListCount());
	}

	private PagerCallback pagerCallback;

	@Override
	public final PagerCallback getPagerCallback() {

		if (pagerCallback == null) {
			final long count = getService().getRepository().getListCount();

			pagerCallback = new PagerCallback() {

				@Override
				public void onRefresh() {
					DefaultController.this.refreshList();
				}

				@Override
				public Translator getTranslator() {
					return DefaultController.this.getTranslator();
				}

				@Override
				public Long getRecordsCount() {
					return count;
				}

				@Override
				public Integer getPageSize() {
					return DefaultController.this.getInitialPageSize();
				}
			};
		}
		return pagerCallback;
	}

	@Override
	public final void refreshList() {

		logger.info("resetList");

		if (entityList != null) {
			entityList.clear();
		}
		entityList = null;

		resetPager();

		getDataModel().setEntityList(getEntityList());
	}

	@Override
	public final void create() {
		try {
			setCurrentEntity(newEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			for (ControllerListener<T> listener : this.getListeners()) {
				canSave = listener.beforePersist(PersistEventType.NEW, entity,
						application);
			}
			if (!canSave) {
				return;
			}
			getService().save(entity);

			for (ControllerListener<T> listener : this.getListeners()) {
				listener.postPersist(PersistEventType.NEW, entity, application);
			}
			refreshList();

			addSuccessMessage(PersistEventType.NEW);
		} catch (Exception ex) {
			addErrorMessage(PersistEventType.NEW, ex);
			// TODO: message
			logger.error(ex.getMessage(), ex);
			for (ControllerListener<T> listener : this.getListeners()) {
				listener.onPersistError(PersistEventType.NEW, entity,
						application, ex);
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
			for (ControllerListener<T> listener : this.getListeners()) {
				canSave = listener.beforePersist(PersistEventType.UPDATE,
						entity, application);
			}
			if (!canSave) {
				return;
			}
			getService().update(entity);

			for (ControllerListener<T> listener : this.getListeners()) {
				listener.postPersist(PersistEventType.UPDATE, entity,
						application);
			}
			refreshList();

			addSuccessMessage(PersistEventType.UPDATE);
		} catch (Exception ex) {
			addErrorMessage(PersistEventType.UPDATE, ex);
			// TODO: message
			logger.error(ex.getMessage(), ex);
			for (ControllerListener<T> listener : this.getListeners()) {
				listener.onPersistError(PersistEventType.UPDATE, entity,
						application, ex);
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
				for (ControllerListener<T> listener : this.getListeners()) {
					canDelete = listener.beforePersist(PersistEventType.EDIT,
							entity, application);
				}
				if (!canDelete) {
					return;
				}
				getService().delete(entity);

				for (ControllerListener<T> listener : this.getListeners()) {
					listener.postPersist(PersistEventType.EDIT, entity,
							application);
				}
			} catch (Exception ex) {
				addErrorMessage(PersistEventType.UPDATE, ex);
				// TODO: message
				logger.error(ex.getMessage(), ex);
				for (ControllerListener<T> listener : this.getListeners()) {
					listener.onPersistError(PersistEventType.UPDATE, entity,
							application, ex);
				}
			}
		}
		refreshList();

		addSuccessMessage(PersistEventType.UPDATE);
	}

	@Override
	public final T getCurrentEntity() {
		logger.debug("currentEntity: {0}", new Object[] { currentEntity });

		return currentEntity;
	}

	@SuppressWarnings("unchecked")
	public final Class<T> getEntityClass() {
		return (Class<T>) this.config.targetEntity();
	}

	@Override
	public final void setCurrentEntity(T currentEntity) {
		refreshList();
		this.currentEntity = currentEntity;
	}

	@Override
	public final void setCurrentEntity(Serializable entityId) {

		try {
			this.setCurrentEntity(getService().find(entityId));
		} catch (Exception e) {
			// TODO MESSAGE
			logger.error("-> Exception", e);

			this.addTranslatedMessage(MessageLevel.ERROR,
					ApplicationKeys.Entity.NOT_FOUND_SUMARY,
					ApplicationKeys.Entity.NOT_FOUND_MSG, getName());
		}
	}

	@Override
	public final List<T> getEntityList() {
		if (entityList == null) {

			if (getInitialPageSize() > -1) {
				entityList = getService().getRepository().findList(getOffset(),
						getPageSize());
			} else {
				entityList = getService().getRepository().findList();
			}
		}
		return entityList;
	}

	@Override
	public void setEntityList(List<T> entityList) {

		if (entityList != null)
			this.entityList = entityList;
		else
			this.entityList.clear();
	}

	protected final String translateIfNecessary(String name, String translateKey) {
		if (java.util.regex.Pattern.matches("\\{[A-Za-z0-9\\.]+\\}",
				translateKey)) {
			translateKey = translateKey.replaceAll("[\\{\\}]", "");
			name = ParserUtils.assertNotEquals(
					this.getTranslator().getMessage(translateKey), name, name);
		}
		return name;
	}

	@Override
	public final String getNameAsList() {

		String name = getName();
		if (java.util.regex.Pattern.matches("\\{[A-Za-z0-9\\.]+\\}", name)) {
			name = "{" + name.replaceAll("[\\{\\}]", "") + ".list}";
		} else {
			name += ".list";
		}
		name = translateIfNecessary(getName(), name);

		logger.debug("nameAsList: {0}", name);

		return name;
	}

	private void addSuccessMessage(PersistEventType executeType) {

		switch (executeType) {
		case NEW:
			addTranslatedMessage(MessageLevel.INFO,
					ApplicationKeys.PersistAction.SAVE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.SAVE_SUCCESS_MESSAGE,
					getName());
			break;
		case UPDATE:
			addTranslatedMessage(MessageLevel.INFO,
					ApplicationKeys.PersistAction.UPDATE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.UPDATE_SUCCESS_MESSAGE,
					getName());
			break;
		case EDIT:
			addTranslatedMessage(MessageLevel.INFO,
					ApplicationKeys.PersistAction.DELETE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.DELETE_SUCCESS_MESSAGE,
					getName());
			break;
		default:
			break;
		}
	}

	private void addErrorMessage(PersistEventType executeType,
			Exception exception) {

		switch (executeType) {
		case NEW:
			addTranslatedMessage(MessageLevel.ERROR,
					ApplicationKeys.PersistAction.SAVE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.SAVE_ERROR_MESSAGE,
					getName(), exception.getMessage());
			break;
		case UPDATE:
			addTranslatedMessage(MessageLevel.ERROR,
					ApplicationKeys.PersistAction.UPDATE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.UPDATE_ERROR_MESSAGE,
					getName(), exception.getMessage());
			break;
		case EDIT:
			addTranslatedMessage(MessageLevel.ERROR,
					ApplicationKeys.PersistAction.DELETE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.DELETE_ERROR_MESSAGE,
					getName(), exception.getMessage());
			break;
		default:
			break;
		}
	}

	@Override
	public final void addTranslatedMessage(MessageLevel level,
			String msgSumary, String msgDetail, Object... args) {

		switch (level) {
		case WARNING:
			application.addWarningMessage(
					this.getTranslator().getMessage(msgSumary), this
							.getTranslator().getMessage(msgDetail, args));
			break;
		case ERROR:
			application.addErrorMessage(
					this.getTranslator().getMessage(msgSumary), this
							.getTranslator().getMessage(msgDetail, args));
			break;
		case INFO:
			application.addInfoMessage(
					this.getTranslator().getMessage(msgSumary), this
							.getTranslator().getMessage(msgDetail, args));
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
	public final Application getApplication() {
		return application;
	}

	@Override
	public final Translator getTranslator() {
		return this.translator;
	}

	@Override
	public Integer getInitialPageSize() {
		return this.config.initialPageSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T newEntity() throws InstantiationException, IllegalAccessException {
		return (T) config.targetEntity().newInstance();
	}

	@Override
	public String getName() {
		return translateIfNecessary(this.config.name(), this.config.name());
	}

	@Override
	public FilterManager getFilter() {
		return FilterManager;
	}
}