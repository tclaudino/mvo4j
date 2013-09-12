package br.com.cd.scaleframework.controller.support;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.cd.scaleframework.controller.DataModel;

public class SimpleDataModel<T> implements DataModel<T> {

	List<T> entityList;

	public SimpleDataModel(List<T> entityList) {
		this.entityList = entityList;
	}

	@Override
	public List<T> getEntityList() {
		return entityList;
	}

	@Override
	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	private int rowIndex;

	final DataModel<T> dataModel = this;
	private Iterator<T> iterator = new Iterator<T>() {

		private int index = 0;

		@Override
		public boolean hasNext() {
			return dataModel.isRowAvailable();
		}

		@Override
		public T next() {
			if (!dataModel.isRowAvailable()) {
				throw new NoSuchElementException();
			}
			T o = dataModel.getRowData();
			dataModel.setRowIndex(++index);
			return o;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	};

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

	@Override
	public boolean isRowAvailable() {
		return rowIndex < getEntityList().size();
	}

	@Override
	public int getRowCount() {
		return getEntityList().size();
	}

	@Override
	public T getRowData() {
		return getEntityList().get(rowIndex);
	}

	@Override
	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
}