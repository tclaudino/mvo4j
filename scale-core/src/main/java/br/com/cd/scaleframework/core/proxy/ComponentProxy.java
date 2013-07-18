package br.com.cd.scaleframework.core.proxy;

import br.com.cd.scaleframework.core.ComponentObject;

@SuppressWarnings("rawtypes")
public interface ComponentProxy<O extends ComponentObject> {

	public static final String PROXY_NO_SETTED = "method setComponentProxy not called";

	void setComponent(O component);

	O getComponent();

}
