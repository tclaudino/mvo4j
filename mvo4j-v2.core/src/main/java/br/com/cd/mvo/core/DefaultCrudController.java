package br.com.cd.mvo.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.CrudController;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.Translator;

public class DefaultCrudController<T> extends DefaultController<T> implements CrudController<T> {

	public static enum ViewMode {

		VIEW, EDIT, NEW, LIST
	}

	private ViewMode viewMode = ViewMode.LIST;

	private List<T> selectedEntityList = new ArrayList<T>();

	public DefaultCrudController(Application application, Translator translator, DataModelFactory modelFactory, CrudService<T> service,
			ControllerMetaData<T> metaData) {
		super(application, translator, modelFactory, service, metaData);
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
		logger.info("toEditMode");

		this.setCurrentEntity(entity);

		viewMode = ViewMode.EDIT;
	}

	@Override
	public void toNewMode() {

		T entity;
		try {
			entity = this.newEntity();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		logger.info("toNewMode");

		this.setCurrentEntity(entity);

		viewMode = ViewMode.NEW;
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
	public final void setSelectedEntities(T selectedEntity, @SuppressWarnings("unchecked") T... selectedEntities) {
		this.selectedEntityList = Arrays.asList(selectedEntities);
		this.selectedEntityList.add(selectedEntity);
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
}