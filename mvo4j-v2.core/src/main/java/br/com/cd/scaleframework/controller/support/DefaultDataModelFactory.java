package br.com.cd.scaleframework.controller.support;

import java.util.List;

import br.com.cd.scaleframework.controller.DataModel;
import br.com.cd.scaleframework.controller.DataModelFactory;

public class DefaultDataModelFactory implements DataModelFactory {

	public <T> DataModel<T> createDataModel(List<T> entityList) {
		return new SimpleDataModel<T>(entityList);
	}
}