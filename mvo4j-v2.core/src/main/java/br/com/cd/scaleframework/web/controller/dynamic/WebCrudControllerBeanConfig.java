package br.com.cd.scaleframework.web.controller.dynamic;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.dynamic.CrudControllerBeanConfig;
import br.com.cd.scaleframework.controller.dynamic.PropertyMap;

public class WebCrudControllerBeanConfig<T, ID extends Serializable> extends
		CrudControllerBeanConfig<T, ID> {

	public WebCrudControllerBeanConfig(PropertyMap adaptee) {
		super(adaptee);
	}

	public static final String LIST_VIEW_NAME = "listViewName";
	public static final String EDIT_VIEW_NAME = "editViewName";
	public static final String CREATE_VIEW_NAME = "createViewName";

	public String listViewName() {
		return this.get(LIST_VIEW_NAME);
	}

	public String editViewName() {
		return this.get(EDIT_VIEW_NAME);
	}

	public String createViewName() {
		return this.get(CREATE_VIEW_NAME);
	}

	public String path() {
		return this.get(WebControllerBeanConfig.PATH);
	}

}