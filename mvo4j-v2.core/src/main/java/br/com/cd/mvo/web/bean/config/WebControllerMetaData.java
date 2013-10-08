package br.com.cd.mvo.web.bean.config;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.PropertyMap;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.web.bean.WebControllerBean;

public class WebControllerMetaData extends ControllerMetaData {

	public static final String PATH = "path";

	public static final String LIST_VIEW_NAME = "listViewName";
	public static final String EDIT_VIEW_NAME = "editViewName";
	public static final String CREATE_VIEW_NAME = "createViewName";

	public WebControllerMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

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
		return this.get(WebControllerMetaData.PATH);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return WebControllerBean.class;
	}

}