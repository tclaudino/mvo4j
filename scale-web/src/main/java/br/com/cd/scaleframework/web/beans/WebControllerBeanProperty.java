package br.com.cd.scaleframework.web.beans;

public enum WebControllerBeanProperty {

	NAME("name"), PATH("path"), INITIAL_PAGE_SIZE("initialPageSize"), MESSAGE_BUNDLE(
			"messageBundle");

	private String property;

	private WebControllerBeanProperty(String property) {
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