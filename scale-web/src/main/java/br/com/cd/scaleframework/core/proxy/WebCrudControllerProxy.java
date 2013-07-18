package br.com.cd.scaleframework.core.proxy;

import java.io.Serializable;

import br.com.cd.scaleframework.core.WebCrudControllerComponent;
import br.com.cd.scaleframework.web.controller.WebCrudController;

public interface WebCrudControllerProxy extends
		CrudControllerProxy<WebCrudControllerComponent>,
		WebCrudController<Object, Serializable> {

}
