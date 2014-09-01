package br.com.cd.mvo.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.ApplicationKeys;
import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ControllerListener;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.FilterManager;
import br.com.cd.mvo.PersistEventType;
import br.com.cd.mvo.Translator;
import br.com.cd.util.ParserUtils;

@SuppressWarnings("rawtypes")
public class DefaultController<T> extends AbstractPageableController implements Controller<T>, Listenable<ControllerListener> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	protected Application application;

	protected Translator translator;

	private DataModelFactory modelFactory;

	protected ControllerMetaData<T> metaData;

	public DefaultController(Application application, Translator translator, DataModelFactory modelFactory, CrudService<T> service,
			ControllerMetaData<T> metaData) {

		this.application = application;
		this.translator = translator;
		this.modelFactory = modelFactory;
		this.metaData = metaData;
		this.service = service;
	}

	private DataModel<T> dataModel;

	private T currentEntity;
	private Collection<T> entityList;

	private CrudService<T> service;

	protected FilterManager FilterManager = new DefaultFilterManager<T>(this);

	@PostConstruct
	@Override
	public void postConstruct() {
		// only proxy listener
	}

	@PreDestroy
	@Override
	public final void preDestroy() {
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

	@Override
	public BeanMetaData<T> getBeanMetaData() {
		return metaData;
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
					return DefaultController.this.getPageSize();
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

		// resetPager();

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
		try {
			getService().save(entity);

			refreshList();

			addSuccessMessage(PersistEventType.NEW);
		} catch (Exception ex) {
			addErrorMessage(PersistEventType.NEW, ex);
			// TODO: message
			logger.error(ex.getMessage(), ex);
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
		try {
			getService().update(entity);

			refreshList();

			addSuccessMessage(PersistEventType.UPDATE);
		} catch (Exception ex) {
			addErrorMessage(PersistEventType.UPDATE, ex);
			// TODO: message
			logger.error(ex.getMessage(), ex);
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
	public final void delete(Collection<T> entityList) {
		for (T entity : entityList) {
			try {
				getService().delete(entity);

				refreshList();

				addSuccessMessage(PersistEventType.DELETE);
			} catch (Exception ex) {
				addErrorMessage(PersistEventType.DELETE, ex);
				// TODO: message
				logger.error(ex.getMessage(), ex);
			}
		}
	}

	@Override
	public final T getCurrentEntity() {
		logger.debug("currentEntity: {0}", new Object[] { currentEntity });

		return currentEntity;
	}

	public final Class<T> getEntityClass() {
		return this.metaData.targetEntity();
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

			this.addTranslatedMessage(MessageLevel.ERROR, ApplicationKeys.Entity.NOT_FOUND_SUMARY, ApplicationKeys.Entity.NOT_FOUND_MSG,
					getName());
		}
	}

	@Override
	public final Collection<T> getEntityList() {
		if (entityList == null) {

			if (getPageSize() > -1) {
				entityList = getService().getRepository().findList(getOffset(), getPageSize());
			} else {
				entityList = getService().getRepository().findList();
			}
		}
		return entityList;
	}

	@Override
	public void setEntityList(Collection<T> entityList) {

		if (entityList != null)
			this.entityList = entityList;
		else
			this.entityList.clear();
	}

	protected final String translateIfNecessary(String name, String translateKey) {
		if (java.util.regex.Pattern.matches("\\{[A-Za-z0-9\\.]+\\}", translateKey)) {
			translateKey = translateKey.replaceAll("[\\{\\}]", "");
			name = ParserUtils.assertNotEquals(this.getTranslator().getMessage(translateKey), name, name);
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
			addTranslatedMessage(MessageLevel.INFO, ApplicationKeys.PersistAction.SAVE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.SAVE_SUCCESS_MESSAGE, getName());
			break;
		case UPDATE:
			addTranslatedMessage(MessageLevel.INFO, ApplicationKeys.PersistAction.UPDATE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.UPDATE_SUCCESS_MESSAGE, getName());
			break;
		case DELETE:
			addTranslatedMessage(MessageLevel.INFO, ApplicationKeys.PersistAction.DELETE_SUCCESS_SUMARY,
					ApplicationKeys.PersistAction.DELETE_SUCCESS_MESSAGE, getName());
			break;
		default:
			break;
		}
	}

	private void addErrorMessage(PersistEventType executeType, Exception exception) {

		switch (executeType) {
		case NEW:
			addTranslatedMessage(MessageLevel.ERROR, ApplicationKeys.PersistAction.SAVE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.SAVE_ERROR_MESSAGE, getName(), exception.getMessage());
			break;
		case UPDATE:
			addTranslatedMessage(MessageLevel.ERROR, ApplicationKeys.PersistAction.UPDATE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.UPDATE_ERROR_MESSAGE, getName(), exception.getMessage());
			break;
		case DELETE:
			addTranslatedMessage(MessageLevel.ERROR, ApplicationKeys.PersistAction.DELETE_ERROR_SUMARY,
					ApplicationKeys.PersistAction.DELETE_ERROR_MESSAGE, getName(), exception.getMessage());
			break;
		default:
			break;
		}
	}

	@Override
	public final void addTranslatedMessage(MessageLevel level, String msgSumary, String msgDetail, Object... args) {

		String message = this.getTranslator().getMessage(msgDetail, args);
		switch (level) {
		case WARNING:
			application.addWarningMessage(this.getTranslator().getMessage(msgSumary), message);
			logger.warn(message);
			break;
		case ERROR:
			application.addErrorMessage(this.getTranslator().getMessage(msgSumary), message);
			logger.error(message);
			break;
		case INFO:
			application.addInfoMessage(this.getTranslator().getMessage(msgSumary), message);
			logger.info(message);
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
		return this.metaData.initialPageSize();
	}

	@Override
	public T newEntity() throws InstantiationException, IllegalAccessException {
		return metaData.targetEntity().newInstance();
	}

	@Override
	public String getName() {
		return translateIfNecessary(this.metaData.name(), this.metaData.name());
	}

	@Override
	public FilterManager getFilter() {
		return FilterManager;
	}

	@Override
	public Class<ControllerListener> getListenerType() {
		return ControllerListener.class;
	}
}