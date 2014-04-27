package br.com.cd.mvo.core;

import java.util.Collection;

public interface DataModel<T> extends Iterable<T> {

	boolean isRowAvailable();

	int getRowCount();

	T getRowData();

	int getRowIndex();

	void setRowIndex(int rowIndex);

	Collection<T> getEntityList();

	void setEntityList(Collection<T> entityList);
}