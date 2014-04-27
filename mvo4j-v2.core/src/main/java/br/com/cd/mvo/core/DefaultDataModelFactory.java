package br.com.cd.mvo.core;

import java.util.Collection;

public class DefaultDataModelFactory implements DataModelFactory {

	public <T> DataModel<T> createDataModel(Collection<T> entityList) {
		return new SimpleDataModel<T>(entityList);
	}
}