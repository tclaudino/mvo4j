package br.com.cd.scaleframework.web.controller.suport;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.AbstractController;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.beans.WebCrudControllerBean;
import br.com.cd.scaleframework.web.controller.WebController;

public abstract class AbstractWebController<T, ID extends Serializable> extends
		AbstractController<T, ID> implements WebController<T, ID> {

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
}