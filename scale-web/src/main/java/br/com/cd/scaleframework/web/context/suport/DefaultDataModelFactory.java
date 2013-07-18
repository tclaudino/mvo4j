package br.com.cd.scaleframework.web.context.suport;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.controller.DataModel;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.controller.SimpleDataModel;

@Component
@Scope("request")
public class DefaultDataModelFactory implements DataModelFactory {

	public <T> DataModel<T> createDataModel(List<T> entityList) {
		return new SimpleDataModel<T>(entityList);
	}
}