package br.com.cd.scaleframework.core.orm.dynamic;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.controller.dynamic.PropertyMap;

public class ServiceBeanConfig<T, ID extends Serializable> extends
		BeanConfig<T, ID> {

	public ServiceBeanConfig(PropertyMap adaptee, Class<T> entityType,
			Class<ID> entityIdType) {
		super(adaptee, entityType, entityIdType);
	}

}