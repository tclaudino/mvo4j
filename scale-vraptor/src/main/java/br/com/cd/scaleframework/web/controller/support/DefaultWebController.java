package br.com.cd.scaleframework.web.controller.support;

import java.io.Serializable;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.controller.DefaultController;
import br.com.cd.scaleframework.core.orm.Service;
import br.com.cd.scaleframework.web.controller.WebController;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBeanConfig;

public class DefaultWebController<T, ID extends Serializable> extends
		DefaultController<T, ID> implements WebController<T, ID> {

	public DefaultWebController(Application application,
			Translator translator, DataModelFactory modelFactory,
			Service<T, ID> service, WebControllerBeanConfig<T, ID> config) {
		super(application, translator, modelFactory, service, config);
	}

}