package br.com.cd.scaleframework.core.orm.dynamic;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.controller.dynamic.PropertyMap;

public class ServiceBeanConfig<T, ID extends Serializable> extends
		BeanConfig<T, ID> {

	public static final String BEAN_NAME_SUFFIX = "Service";
	public static final String ENTITY_MANAGER_FACTORY_QUALIFIER = "entityManagerFactoryQualifier";

	public ServiceBeanConfig(PropertyMap adaptee) {
		super(adaptee);
	}

	public String entityManagerFactoryQualifier() {
		return this.get(ENTITY_MANAGER_FACTORY_QUALIFIER);
	}
}