package br.com.cd.scaleframework.controller;

import java.util.List;

public interface DataModelFactory {

	<T> DataModel<T> createDataModel(List<T> entityList);
}