package br.com.cd.scaleframework.old;

public enum ControllerBeanProperty {

	NAME("name"), PATH("path"), INITIAL_PAGE_SIZE("initialPageSize"), MESSAGE_BUNDLE(
			"messageBundle");

	private String property;

	private ControllerBeanProperty(String property) {
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