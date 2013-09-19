package br.com.cd.scaleframework.beans.factory.ioc;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.controller.dynamic.ControllerBean;
import br.com.cd.scaleframework.controller.dynamic.CrudControllerBean;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBean;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBean;

public class BaseComponents {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Annotation>[] STEREOTYPES = new Class[] {
			CrudControllerBean.class, ControllerBean.class,
			WebCrudControllerBean.class, WebControllerBean.class };

}
