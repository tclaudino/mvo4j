package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class ControllerBeanConfig<T, ID extends Serializable> extends
		BeanConfig<T, ID> {

	public static final String BEAN_NAME_SUFFIX = "Controller";
	public static final String MAKE_LIST = "makeList";
	public static final String LAZY_PROPERTIES = "lazyProperties";
	public static final String ENTITY_MANAGER_FACTORY_QUALIFIER = "entityManagerFactoryQualifier";

	public ControllerBeanConfig(PropertyMap adaptee) {
		super(adaptee);
	}

	public String makeList() {
		return this.get(MAKE_LIST);
	}

	public String lazyProperties() {
		return this.get(LAZY_PROPERTIES);
	}

	public String entityManagerFactoryQualifier() {
		return this.get(ENTITY_MANAGER_FACTORY_QUALIFIER);
	}
}