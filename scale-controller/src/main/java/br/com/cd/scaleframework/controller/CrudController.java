package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.List;

import br.com.cd.scaleframework.controller.suport.FilterManager;

public interface CrudController<T, ID extends Serializable> extends
		Controller<T, ID> {

	void cancelEdit();

	void toViewMode();

	void toUpdateMode();

	void toNewMode();

	void toListMode();

	boolean isListMode();

	boolean isViewMode();

	boolean isUpdateMode();

	boolean isInsertMode();

	boolean isEditable();

	void setSelectedEntity(boolean selected);

	List<T> getSelectedEntityList();

	void setSelectedEntityList(List<T> selectedEntityList);

	T[] getSelectedEntities();

	void setSelectedEntities(T[] selectedEntities);

	String getSelectedField();

	void setSelectedField(String selectedField);

	String getSearchText();

	void setSearchText(String searchText);

	void onSelecteEntity();

	void onSelecteEntity(T entity);

	boolean isSelectedEntity();

	boolean isSelectedEntity(T entity);

	void deleteSelecteds();

	FilterManager getFilter();

	void applyFilter();
}