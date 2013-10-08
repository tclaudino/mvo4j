package br.com.cd.mvo.core;

import java.util.List;

public class DefaultDataModelFactory implements DataModelFactory {

	public <T> DataModel<T> createDataModel(List<T> entityList) {
		return new SimpleDataModel<T>(entityList);
	}
}