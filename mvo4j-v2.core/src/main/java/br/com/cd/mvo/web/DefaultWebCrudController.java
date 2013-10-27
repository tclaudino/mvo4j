package br.com.cd.mvo.web;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.core.ListenableController;
import br.com.cd.mvo.util.ParserUtils;
import br.com.cd.mvo.util.StringUtils;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class DefaultWebCrudController<T> extends DefaultCrudController<T>
		implements WebCrudController<T>, ListenableController<T> {

	private WebControllerMetaData _config;

	public DefaultWebCrudController(Application application,
			Translator translator, DataModelFactory modelFactory,
			CrudService<T> service, WebControllerMetaData config) {
		super(application, translator, modelFactory, service, config);

		this._config = config;
	}

	@Override
	public final String getViewsFolder() {
		String view = translateIfNecessary(this._config.path(),
				this._config.path());

		view = StringUtils.removeBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");

		return view;
	}

	@Override
	public final String getListView() {
		String view = translateIfNecessary(this._config.listViewName(),
				this._config.listViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getEditView() {
		String view = translateIfNecessary(this._config.editViewName(),
				this._config.editViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getCreateView() {
		String view = translateIfNecessary(this._config.createViewName(),
				this._config.createViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}
}