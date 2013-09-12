package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.List;

public interface CrudController<T, ID extends Serializable> extends
		Controller<T, ID> {

	void cancelEdit();

	void toViewMode();

	void toEditMode();

	void toNewMode();

	void toListMode();

	boolean isListMode();

	boolean isViewMode();

	boolean isEditMode();

	boolean isNewMode();

	boolean isEditable();

	void setSelectedCurrentEntity(boolean selected);

	T[] getSelectedEntities();

	List<T> getSelectedEntityList();

	void setSelectedEntityList(List<T> selectedEntityList);

	void setSelectedEntities(T... selectedEntities);

	void onSelectCurrentEntity();

	void onSelectEntity(T entity);

	boolean isSelectedCurrentEntity();

	boolean isSelectedEntity(T entity);

	void deleteSelecteds();
}