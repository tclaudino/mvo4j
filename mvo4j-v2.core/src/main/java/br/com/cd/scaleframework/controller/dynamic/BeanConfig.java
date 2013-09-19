package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class BeanConfig<T, ID extends Serializable> extends PropertyMapAdapter {

	public static final String NAME = "name";
	public static final String SCOPE = "scope";
	public static final String INITIAL_PAGE_SIZE = "initialPageSize";
	public static final String MESSAGE_BUNDLE = "messageBundle";
	public static final String TARGET_ENTITY = "targetEntity";
	public static final String ENTITY_ID_TYPE = "entityIdType";

	public BeanConfig(PropertyMap adaptee) {
		super(adaptee);
	}

	public String name() {
		return this.get(BeanConfig.NAME);
	}

	public String scope() {
		return this.get(SCOPE);
	}

	public int initialPageSize() {
		return this.getInt(BeanConfig.INITIAL_PAGE_SIZE);
	}

	public String messageBundle() {
		return this.get(BeanConfig.MESSAGE_BUNDLE);
	}

	@SuppressWarnings("unchecked")
	public Class<T> targetEntity() {
		return (Class<T>) this.get(BeanConfig.TARGET_ENTITY, Class.class);
	}

	@SuppressWarnings("unchecked")
	public Class<ID> entityIdType() {
		return (Class<ID>) this.getAsType(BeanConfig.ENTITY_ID_TYPE,
				Serializable.class);
	}

	@Override
	public String toString() {
		return "[name()=" + name() + ", targetEntity()=" + targetEntity()
				+ ", entityIdType=" + entityIdType() + "]";
	}

}