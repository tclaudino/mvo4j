package br.com.cd.scaleframework.web.beans;

public enum WebCrudControllerBeanProperty {

	NAME("name"), PATH("path"), INITIAL_PAGE_SIZE("initialPageSize"), MESSAGE_BUNDLE(
			"messageBundle"), LIST_VIEW_NAME("listViewName"), EDIT_VIEW_NAME(
			"editViewName"), CREATE_VIEW_NAME("createViewName");

	private String property;

	private WebCrudControllerBeanProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	@Override
	public String toString() {
		return property;
	}
}