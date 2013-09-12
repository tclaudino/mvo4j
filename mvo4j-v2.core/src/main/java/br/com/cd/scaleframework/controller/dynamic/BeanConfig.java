package br.com.cd.scaleframework.controller.dynamic;

import java.io.Serializable;

public class BeanConfig<T, ID extends Serializable> extends PropertyMapAdapter {

	public static final String NAME = "name";
	public static final String INITIAL_PAGE_SIZE = "initialPageSize";
	public static final String MESSAGE_BUNDLE = "messageBundle";
	public static final String TARGET_ENTITY = "targetEntity";
	public static final String ENTITY_ID_TYPE = "entityIdType";

	private Class<T> entityType;
	private Class<ID> entityIdType;

	public BeanConfig(PropertyMap adaptee, Class<T> entityType,
			Class<ID> entityIdType) {
		super(adaptee);
		this.entityType = entityType;
		this.entityIdType = entityIdType;
	}

	public String name() {
		return this.get(BeanConfig.NAME);
	}

	public int initialPageSize() {
		return this.getInt(BeanConfig.INITIAL_PAGE_SIZE);
	}

	public String messageBundle() {
		return this.get(BeanConfig.MESSAGE_BUNDLE);
	}

	public Class<T> targetEntity() {
		return this.getAsType(BeanConfig.TARGET_ENTITY, entityType);
	}

	public Class<ID> entityIdType() {
		return this.getAsType(BeanConfig.ENTITY_ID_TYPE, entityIdType);
	}

}