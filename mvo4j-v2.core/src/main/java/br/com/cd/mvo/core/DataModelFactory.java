package br.com.cd.mvo.core;

import java.util.Collection;

public interface DataModelFactory {

	public String BEAN_NAME = DataModelFactory.class.getName();

	<T> DataModel<T> createDataModel(Collection<T> entityList);
}