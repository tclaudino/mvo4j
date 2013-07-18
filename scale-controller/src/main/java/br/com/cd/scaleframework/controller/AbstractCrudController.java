package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import br.com.cd.scaleframework.context.TranslatorKeys;
import br.com.cd.scaleframework.controller.suport.FilterManager;
import br.com.cd.scaleframework.util.StringUtils;

public abstract class AbstractCrudController<T, ID extends Serializable>
		extends AbstractController<T, ID> implements CrudController<T, ID> {

	public static enum ViewMode {

		VIEW, UPDATE, INSERT, LIST
	}

	private ViewMode viewMode = ViewMode.LIST;

	private List<T> selectedEntityList = new ArrayList<T>();
	private T[] selectedEntities;

	private FilterManager filterManager = new FilterManager();

	private String selectedField = "";
	private String searchText = "";

	@Override
	public final void cancelEdit() {
		this.toViewMode();
	}

	@Override
	public void toViewMode() {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"toViewMode");

		if (this.isListMode()) {

			DataModel<T> entList = getDataModel();
			if (entList.isRowAvailable() && entList.getRowData() != null) {
				this.setCurrentEntity(entList.getRowData());
			}
		}

		viewMode = ViewMode.VIEW;
	}

	@Override
	public void toUpdateMode() {

		T entity = this.getCurrentEntity();
		if (this.isListMode()) {

			DataModel<T> entList = getDataModel();
			if (entList.isRowAvailable() && entList.getRowData() != null) {
				entity = entList.getRowData();
			}
		}
		boolean canUpdate = true;
		for (EventListener<T> listener : this.getListeners()) {
			canUpdate = listener.before(EventType.UPDATE, entity,
					getMessenger(), getTranslator());
			if (!canUpdate)
				break;
		}
		if (canUpdate) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"toEditMode");

			this.setCurrentEntity(entity);

			viewMode = ViewMode.UPDATE;
		} else {
			this.addMessage(
					MessageLevel.WARNING,
					getTranslator().getMessage(
							TranslatorKeys.Publisher.AcccessDenied.SUMARY),
					getTranslator().getMessage(
							TranslatorKeys.Publisher.AcccessDenied.UPDATE));
		}
	}

	@Override
	public void toNewMode() {

		T entity = this.newEntity();

		boolean canInsert = true;
		for (EventListener<T> listener : this.getListeners()) {
			canInsert = listener.before(EventType.INSERT, entity,
					getMessenger(), getTranslator());
			if (!canInsert)
				break;
		}
		if (canInsert) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"toEditMode");

			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"toNewMode");

			this.setCurrentEntity(entity);

			viewMode = ViewMode.INSERT;
		} else {
			this.addMessage(
					MessageLevel.WARNING,
					getTranslator().getMessage(
							TranslatorKeys.Publisher.AcccessDenied.SUMARY),
					getTranslator().getMessage(
							TranslatorKeys.Publisher.AcccessDenied.INSERT));
		}
	}

	@Override
	public void toListMode() {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"toListMode");

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
	public final boolean isUpdateMode() {
		return ViewMode.UPDATE.equals(viewMode);
	}

	@Override
	public final boolean isInsertMode() {
		return ViewMode.INSERT.equals(viewMode);
	}

	@Override
	public final boolean isEditable() {
		return isUpdateMode() || isInsertMode();
	}

	@Override
	public final List<T> getSelectedEntityList() {
		return selectedEntityList;
	}

	@Override
	public final void setSelectedEntityList(List<T> selectedEntityList) {
		if (selectedEntities != null) {
			this.selectedEntityList = selectedEntityList;
		} else {
			this.selectedEntityList = Collections.emptyList();
		}
	}

	@Override
	public final T[] getSelectedEntities() {
		return selectedEntities;
	}

	@Override
	public final void setSelectedEntities(T[] selectedEntities) {
		this.selectedEntities = selectedEntities;
		setSelectedEntityList(Arrays.asList(selectedEntities));
	}

	@Override
	public final void setSelectedEntity(boolean selected) {
		onSelecteEntity(getCurrentEntity());
	}

	@Override
	public final String getSelectedField() {
		return selectedField;
	}

	@Override
	public final void setSelectedField(String selectedField) {
		this.selectedField = selectedField;
	}

	@Override
	public final String getSearchText() {
		return searchText;
	}

	@Override
	public final void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	@Override
	public final void onSelecteEntity() {
		T entity = getCurrentEntity();
		if (entity == null) {

			DataModel<T> dataModel = getDataModel();
			if (dataModel.isRowAvailable() && dataModel.getRowData() != null) {
				entity = dataModel.getRowData();
			}
		}
		onSelecteEntity(entity);
	}

	@Override
	public final void onSelecteEntity(T entity) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"onSelecteEntity, entity: {0}", new Object[] { entity });
		if (entity != null) {
			if (selectedEntityList.contains(entity)) {
				selectedEntityList.remove(entity);
			} else {
				selectedEntityList.add(entity);
			}
		}
	}

	@Override
	public final boolean isSelectedEntity() {
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
		boolean selected = entity != null
				&& selectedEntityList.contains(entity);
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"isSelectedEntity, entity: {0}, selected: {1}",
				new Object[] { entity, selected });

		return selected;
	}

	@Override
	public final void deleteSelecteds() {
		delete(selectedEntityList);
	}

	@Override
	public FilterManager getFilter() {
		return filterManager;
	}

	@Override
	public final void applyFilter() {
		boolean success = false;

		if (!StringUtils.isNullOrEmpty(selectedField)
				&& !StringUtils.isNullOrEmpty(searchText)) {

			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"applyFilter, selectedField: {0}, searchText: {1}",
					new Object[] { selectedField, searchText });

			try {
				filterManager.add(selectedField, searchText);
				List<T> entityList;
				if (getInitialPageSize() > -1) {
					entityList = getService().findList(
							filterManager.getFilterMap(), getOffset(),
							getPageSize());
				} else {
					entityList = getService().findList(
							filterManager.getFilterMap());
				}
				setEntityList(entityList);
				success = true;
			} catch (Exception e) {
				Logger.getLogger(this.getClass().getName())
						.log(Level.INFO,
								"applyFilter.INVALID_SEARCH, selectedField: {0}, searchText: {1}",
								new Object[] { selectedField, searchText });
			}
		}
		if (!success) {
			addMessage(MessageLevel.WARNING,
					TranslatorKeys.Entity.NOT_FOUND_SUMARY,
					TranslatorKeys.Entity.NOT_FOUND_MSG,
					new Object[] { getName() });
		}
	}

}