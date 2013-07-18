package br.com.cd.scaleframework.core;

import java.lang.annotation.Annotation;

import br.com.cd.scaleframework.core.proxy.ComponentProxy;

@SuppressWarnings("rawtypes")
public interface ComponentListableFactory<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation>
		extends ComponentFactory<O, P, A>, ComponentRegistry<O> {

}
