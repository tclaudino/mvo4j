package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Application.MessageLevel;
import br.com.cd.scaleframework.context.ApplicationKeys;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.dynamic.CrudControllerBeanConfig;
import br.com.cd.scaleframework.controller.support.DefaultFilterManager;
import br.com.cd.scaleframework.core.orm.Service;

public class DefaultCrudController<T, ID extends Serializable> extends
		DefaultController<T, ID> implements CrudController<T, ID> {

	public static enum ViewMode {

		VIEW, EDIT, NEW, LIST
	}

	private ViewMode viewMode = ViewMode.LIST;

	private List<T> selectedEntityList = new ArrayList<T>();

	private FilterManager filterManager = new DefaultFilterManager<T>(this);

	public DefaultCrudController(Application application,
			Translator translator, DataModelFactory modelFactory,
			Service<T, ID> service, CrudControllerBeanConfig<T, ID> config) {
		super(application, translator, modelFactory, service, config);
	}

	@Override
	public final void cancelEdit() {
		this.toViewMode();
	}

	@Override
	public void toViewMode() {
		logger.info("toViewMode");

		if (this.isListMode()) {

			DataModel<T> entList = getDataModel();
			if (entList.isRowAvailable() && entList.getRowData() != null) {
				this.setCurrentEntity(entList.getRowData());
			}
		}

		viewMode = ViewMode.VIEW;
	}

	@Override
	public void toEditMode() {

		T entity = this.getCurrentEntity();
		if (this.isListMode()) {

			DataModel<T> entList = getDataModel();
			if (entList.isRowAvailable() && entList.getRowData() != null) {
				entity = entList.getRowData();
			}
		}
		boolean canUpdate = true;
		for (ControllerListener<T> listener : this.getListeners()) {
			canUpdate = listener.beforePersist(PersistEventType.UPDATE, entity,
					getApplication());
			if (!canUpdate)
				break;
		}
		if (canUpdate) {
			logger.info("toEditMode");

			this.setCurrentEntity(entity);

			viewMode = ViewMode.EDIT;
		} else {
			this.addTranslatedMessage(
					MessageLevel.WARNING,
					getTranslator().getMessage(
							ApplicationKeys.Publisher.AcccessDenied.SUMARY),
					getTranslator().getMessage(
							ApplicationKeys.Publisher.AcccessDenied.UPDATE));
		}
	}

	@Override
	public void toNewMode() {

		T entity;
		try {
			entity = this.newEntity();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		boolean canInsert = true;
		for (ControllerListener<T> listener : this.getListeners()) {
			canInsert = listener.beforePersist(PersistEventType.NEW, entity,
					getApplication());
			if (!canInsert)
				break;
		}
		if (canInsert) {
			logger.info("toNewMode");

			this.setCurrentEntity(entity);

			viewMode = ViewMode.NEW;
		} else {
			this.addTranslatedMessage(
					MessageLevel.WARNING,
					getTranslator().getMessage(
							ApplicationKeys.Publisher.AcccessDenied.SUMARY),
					getTranslator().getMessage(
							ApplicationKeys.Publisher.AcccessDenied.INSERT));
		}
	}

	@Override
	public void toListMode() {
		logger.info("toListMode");

		setCurrentEntity((T) null);
		viewMode = ViewMode.LIST;
	}

	@Override
	public final boolean isListMode() {
		return ViewMode.LIST.equals(viewMode);
	}

	@Override
	public final boolean isViewMode() {
		return ViewMode.VIEW.equals(viewMode);
	}

	@Override
	public final boolean isEditMode() {
		return ViewMode.EDIT.equals(viewMode);
	}

	@Override
	public final boolean isNewMode() {
		return ViewMode.NEW.equals(viewMode);
	}

	@Override
	public final boolean isEditable() {
		return isEditMode() || isNewMode();
	}

	@Override
	public final List<T> getSelectedEntityList() {
		return Collections.unmodifiableList(selectedEntityList);
	}

	@Override
	public final void setSelectedEntityList(List<T> selectedEntityList) {
		if (selectedEntityList != null) {
			this.selectedEntityList = selectedEntityList;
		} else {
			this.selectedEntityList = Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T[] getSelectedEntities() {
		return (T[]) this.getSelectedEntityList().toArray();
	}

	@Override
	public final void setSelectedEntities(T... selectedEntities) {
		this.selectedEntityList = Arrays.asList(selectedEntities);
	}

	@Override
	public final void setSelectedCurrentEntity(boolean selected) {
		onSelectEntity(getCurrentEntity());
	}

	@Override
	public final void onSelectCurrentEntity() {
		T entity = getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}
		onSelectEntity(entity);
	}

	@Override
	public final void onSelectEntity(T entity) {
		logger.info("onSelecteEntity, entity: {0}", new Object[] { entity });
		if (entity != null) {
			if (selectedEntityList.contains(entity)) {
				selectedEntityList.remove(entity);
			} else {
				selectedEntityList.add(entity);
			}
		}
	}

	@Override
	public final boolean isSelectedCurrentEntity() {
		T entity = getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}

		return isSelectedEntity(entity);
	}

	@Override
	public final boolean isSelectedEntity(T entity) {
		return entity != null && selectedEntityList.contains(entity);
	}

	@Override
	public final void deleteSelecteds() {
		delete(selectedEntityList);
	}

	@Override
	public FilterManager getFilter() {
		return filterManager;
	}
}