package br.com.cd.scaleframework.core;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.core.proxy.ComponentProxy;

@SuppressWarnings("rawtypes")
public interface ControllerComponentFactory<O extends ControllerComponent, P extends ComponentProxy<O>, A extends Annotation>
		extends ComponentListableFactory<O, P, A> {

	int getInitialPageSize();

	void setInitialPageSize(int initialPageSize);

}