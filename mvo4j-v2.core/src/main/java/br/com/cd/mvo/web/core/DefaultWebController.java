package br.com.cd.mvo.web.core;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultController;
import br.com.cd.mvo.web.WebController;

public class DefaultWebController<T> extends DefaultController<T> implements WebController<T> {

	public DefaultWebController(Application application, Translator translator, DataModelFactory modelFactory, CrudService<T> service,
			WebControllerMetaData<T> config) {
		super(application, translator, modelFactory, service, config);
	}

}