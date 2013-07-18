package br.com.cd.scaleframework.controller;

public interface DataModel<T> extends Iterable<T> {

	boolean isRowAvailable();

	int getRowCount();

	T getRowData();

	int getRowIndex();

	void setRowIndex(int rowIndex);

	java.util.List<T> getEntityList();

	void setEntityList(java.util.List<T> entityList);
}