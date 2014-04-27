package br.com.cd.mvo.web.core;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.util.ParserUtils;
import br.com.cd.util.StringUtils;

public class DefaultWebCrudController<T> extends DefaultCrudController<T> implements WebCrudController<T> {

	private WebControllerMetaData<T> metaData;

	public DefaultWebCrudController(Application application, Translator translator, DataModelFactory modelFactory, CrudService<T> service,
			WebControllerMetaData<T> config) {
		super(application, translator, modelFactory, service, config);

		this.metaData = config;
	}

	@Override
	public final String getViewsFolder() {
		String view = translateIfNecessary(this.metaData.path(), this.metaData.path());

		view = StringUtils.removeBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");

		return view;
	}

	@Override
	public final String getListView() {
		String view = translateIfNecessary(this.metaData.listViewName(), this.metaData.listViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getEditView() {
		String view = translateIfNecessary(this.metaData.editViewName(), this.metaData.editViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}

	@Override
	public final String getCreateView() {
		String view = translateIfNecessary(this.metaData.createViewName(), this.metaData.createViewName());

		view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
		view = ParserUtils.assertNotEquals(view, "/", "");
		view = getViewsFolder() + view;

		return view;
	}
}