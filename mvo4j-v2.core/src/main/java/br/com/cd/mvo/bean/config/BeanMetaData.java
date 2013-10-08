package br.com.cd.mvo.bean.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.PropertyMap;
import br.com.cd.mvo.bean.PropertyMapAdapter;

public abstract class BeanMetaData extends PropertyMapAdapter {

	public static final String NAME = "name";
	public static final String SCOPE = "scope";
	public static final String INITIAL_PAGE_SIZE = "initialPageSize";
	public static final String MESSAGE_BUNDLE = "messageBundle";
	public static final String TARGET_ENTITY = "targetEntity";
	public static final String ENTITY_ID_TYPE = "entityIdType";

	public static final String MAKE_LIST = "makeList";
	public static final String LAZY_PROPERTIES = "lazyProperties";
	public static final String PERSISTENCE_FACTORY_QUALIFIER = "persistenceFactoryQualifier";
	public static final String PERSISTENCE_PROVIDER = "persistenceProvider";

	public BeanMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

	public String name() {
		return this.get(BeanMetaData.NAME);
	}

	public String scope() {
		return this.get(SCOPE);
	}

	public int initialPageSize() {
		return this.getInt(BeanMetaData.INITIAL_PAGE_SIZE);
	}

	public String messageBundle() {
		return this.get(BeanMetaData.MESSAGE_BUNDLE);
	}

	public Class<?> targetEntity() {
		return (Class<?>) this.get(BeanMetaData.TARGET_ENTITY, Class.class);
	}

	public Class<Serializable> entityIdType() {
		return (Class<Serializable>) this.getAsType(
				BeanMetaData.ENTITY_ID_TYPE, Serializable.class);
	}

	public String makeList() {
		return this.get(MAKE_LIST);
	}

	public String lazyProperties() {
		return this.get(LAZY_PROPERTIES);
	}

	public String persistenceManagerQualifier() {
		return this.get(PERSISTENCE_FACTORY_QUALIFIER);
	}

	public String persistenceProvider() {
		return this.get(PERSISTENCE_PROVIDER);
	}

	@Override
	public String toString() {
		return "[name()=" + name() + ", targetEntity()=" + targetEntity()
				+ ", entityIdType=" + entityIdType() + "]";
	}

	public abstract Class<? extends Annotation> annotationType();

	public abstract String getBeanNameSuffix();

}