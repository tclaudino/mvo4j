package br.com.cd.mvo.core;

import java.util.List;

public interface DataModelFactory {

	public String BEAN_NAME = DataModelFactory.class.getName();

	<T> DataModel<T> createDataModel(List<T> entityList);
}