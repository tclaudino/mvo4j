package br.com.cd.mvo.web.core;

import br.com.cd.mvo.core.ControllerMetaData;
import br.com.cd.mvo.core.MetaData;

public class WebControllerMetaData<T> extends ControllerMetaData<T> {

	public static final String BEAN_NAME_SUFFIX = "WebController";

	public static final String LIST_VIEW_NAME = "listViewName";
	public static final String EDIT_VIEW_NAME = "editViewName";
	public static final String CREATE_VIEW_NAME = "createViewName";

	public WebControllerMetaData(MetaData adaptee) {
		super(adaptee);
	}

	public String listViewName() {
		return this.get(LIST_VIEW_NAME, "list");
	}

	public String editViewName() {
		return this.get(EDIT_VIEW_NAME, "edit");
	}

	public String createViewName() {
		return this.get(CREATE_VIEW_NAME, "create");
	}

	@Override
	public String getBeanNameSuffix() {
		return WebControllerMetaData.BEAN_NAME_SUFFIX;
	}

}