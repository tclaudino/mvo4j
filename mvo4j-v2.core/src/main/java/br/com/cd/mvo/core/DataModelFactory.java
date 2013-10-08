package br.com.cd.mvo.core;

import java.util.List;

public interface DataModelFactory {

	<T> DataModel<T> createDataModel(List<T> entityList);
}