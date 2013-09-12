package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class ControllerBeanConfig<T, ID extends Serializable> extends
		BeanConfig<T, ID> {

	public static final String SCOPE = "scope";
	public static final String MAKE_LIST = "makeList";
	public static final String LAZY_PROPERTIES = "lazyProperties";
	public static final String SESSION_FACTORY_QUALIFIER = "sessionFactoryQualifier";

	public ControllerBeanConfig(PropertyMap adaptee, Class<T> entityType,
			Class<ID> entityIdType) {
		super(adaptee, entityType, entityIdType);
	}

	public String name() {
		return this.get(NAME);
	}

	public int initialPageSize() {
		return this.getInt(INITIAL_PAGE_SIZE);
	}

	public String scope() {
		return this.get(SCOPE);
	}

	public String makeList() {
		return this.get(MAKE_LIST);
	}

	public String lazyProperties() {
		return this.get(LAZY_PROPERTIES);
	}

	public String sessionFactoryQualifier() {
		return this.get(SESSION_FACTORY_QUALIFIER);
	}

	public String messageBundle() {
		return this.get(MESSAGE_BUNDLE);
	}
}