package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class CrudControllerBeanConfig<T, ID extends Serializable> extends
		ControllerBeanConfig<T, ID> {

	public CrudControllerBeanConfig(PropertyMap adaptee) {
		super(adaptee);
		// TODO Auto-generated constructor stub
	}

	// RelationMap[] relationMaps() default {};

}