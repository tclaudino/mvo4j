package br.com.cd.mvo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleDataModel<T> implements DataModel<T> {

	Collection<T> entityList;

	public SimpleDataModel(Collection<T> entityList) {
		this.entityList = entityList;
	}

	@Override
	public Collection<T> getEntityList() {
		return entityList;
	}

	@Override
	public void setEntityList(Collection<T> entityList) {
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
		return new ArrayList<T>(getEntityList()).get(rowIndex);
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