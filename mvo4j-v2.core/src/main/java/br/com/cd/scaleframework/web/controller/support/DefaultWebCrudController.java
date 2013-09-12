package br.com.cd.scaleframework.web.controller.support;

import java.io.Serializable;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.controller.DefaultCrudController;
import br.com.cd.scaleframework.core.orm.Service;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.controller.WebCrudController;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;

public class DefaultWebCrudController<T, ID extends Serializable> extends
		DefaultCrudController<T, ID> implements WebCrudController<T, ID> {

	public DefaultWebCrudController(Application application,
			Translator translator, DataModelFactory modelFactory,
			Service<T, ID> service, WebCrudControllerBeanConfig<T, ID> config) {
		super(application, translator, modelFactory, service, config);
	}

	@Override
	public final String getViewsFolder() {
		String view = translateIfNecessary(this.config.path(),
				this.config.path());

		view = StringUtils.removeBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");

		return view;
	}

	@Override
	public final String getListView() {
		String view = translateIfNecessary(this.config.listViewName(),
				this.config.listViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getEditView() {
		String view = translateIfNecessary(this.config.editViewName(),
				this.config.editViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getCreateView() {
		String view = translateIfNecessary(this.config.createViewName(),
				this.config.createViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}
}