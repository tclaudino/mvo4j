package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class CrudControllerBeanConfig<T, ID extends Serializable> extends
		ControllerBeanConfig<T, ID> {

	public CrudControllerBeanConfig(PropertyMap adaptee, Class<T> entityType,
			Class<ID> entityIdType) {
		super(adaptee, entityType, entityIdType);
		// TODO Auto-generated constructor stub
	}

	// RelationMap[] relationMaps() default {};

}