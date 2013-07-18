package br.com.cd.scaleframework.web.controller.suport;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.AbstractCrudController;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.beans.WebCrudControllerBean;
import br.com.cd.scaleframework.web.controller.WebCrudController;

public abstract class AbstractWebCrudController<T, ID extends Serializable>
		extends AbstractCrudController<T, ID> implements
		WebCrudController<T, ID> {

	@Override
	public Integer getInitialPageSize() {
		int pageSize = 0;
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = this.getClass().getAnnotation(
					WebCrudControllerBean.class);
			pageSize = config.initialPageSize();
		}
		return pageSize;
	}

	@Override
	public final String getName() {
		String view = "";
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = getClass().getAnnotation(WebCrudControllerBean.class);
			view = translateIfNecessary(config.name(), config.name());
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"name: {0}", view);

		return view;
	}

	@Override
	protected void applyBundle(Translator translator) {

		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = getClass().getAnnotation(WebCrudControllerBean.class);
			if (StringUtils.isNullOrEmpty(config.messageBundle())) {
				String bundleName = config.messageBundle();
				Logger.getLogger(this.getClass().getName()).log(Level.INFO,
						"bundleName: {0}", bundleName);

				translator.setBundleName(bundleName);
			}
		}
	}

	@Override
	public final String getViewsFolder() {
		String view = "";
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = getClass().getAnnotation(WebCrudControllerBean.class);
			view = translateIfNecessary(config.path(), config.path());

			view = StringUtils.removeBeginSlash(StringUtils
					.removeEndSlash(view));
			view = ParserUtils.assertNotEquals(view, "/", "");
		}

		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"viewsFolder: {0}", view);

		return view;
	}

	@Override
	public final String getListView() {
		String view = "";
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = this.getClass().getAnnotation(
					WebCrudControllerBean.class);
			view = translateIfNecessary(config.listViewName(),
					config.listViewName());

			view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
			view = ParserUtils.assertNotEquals(view, "/", "");
			view = getViewsFolder() + view;
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"listViewName: {0}", view);

		return view;
	}

	@Override
	public final String getEditView() {
		String view = "";
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = getClass().getAnnotation(WebCrudControllerBean.class);
			view = translateIfNecessary(config.editViewName(),
					config.editViewName());

			view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
			view = ParserUtils.assertNotEquals(view, "/", "");
			view = getViewsFolder() + view;
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"editViewName: {0}", view);

		return view;
	}

	@Override
	public final String getCreateView() {
		String view = "";
		if (this.getClass().isAnnotationPresent(WebCrudControllerBean.class)) {
			WebCrudControllerBean config = getClass().getAnnotation(WebCrudControllerBean.class);
			view = translateIfNecessary(config.createViewName(),
					config.createViewName());

			view = StringUtils.addBeginSlash(StringUtils.removeEndSlash(view));
			view = ParserUtils.assertNotEquals(view, "/", "");
			view = getViewsFolder() + view;
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"createViewName: {0}", view);

		return view;
	}
}