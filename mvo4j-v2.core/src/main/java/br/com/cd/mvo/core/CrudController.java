package br.com.cd.mvo.core;

import java.util.List;

public interface CrudController<T> extends Controller<T> {

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

	void setSelectedEntities(T selectedEntity, @SuppressWarnings("unchecked") T... selectedEntities);

	void onSelectCurrentEntity();

	void onSelectEntity(T entity);

	boolean isSelectedCurrentEntity();

	boolean isSelectedEntity(T entity);

	void deleteSelecteds();
}