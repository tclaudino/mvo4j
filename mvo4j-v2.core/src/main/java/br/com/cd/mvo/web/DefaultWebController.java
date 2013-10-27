package br.com.cd.mvo.web;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultController;
import br.com.cd.mvo.core.ListenableController;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class DefaultWebController<T> extends DefaultController<T> implements
		WebController<T>, ListenableController<T> {

	public DefaultWebController(Application application, Translator translator,
			DataModelFactory modelFactory, CrudService<T> service,
			WebControllerMetaData config) {
		super(application, translator, modelFactory, service, config);
	}

}