package br.com.cd.mvo.bean.config;

import java.io.Serializable;

public abstract class DefaultBeanMetaData<T> extends BeanMetaDataDelegate<T> {

	private final Class<T> targetEntity;

	public DefaultBeanMetaData(Class<T> targetEntity, MetaData adaptee) {
		super(adaptee);
		this.targetEntity = targetEntity;
	}

	@Override
	public String name() {
		return this.get(BeanMetaData.NAME);
	}

	@Override
	public String scope() {
		return this.get(SCOPE);
	}

	@Override
	public int initialPageSize() {
		return this.getInt(BeanMetaData.INITIAL_PAGE_SIZE);
	}

	@Override
	public String messageBundle() {
		return this.get(BeanMetaData.MESSAGE_BUNDLE);
	}

	@Override
	public Class<T> targetEntity() {
		return targetEntity;
	}

	@Override
	public Class<Serializable> entityIdType() {
		return (Class<Serializable>) this.getAsType(BeanMetaData.ENTITY_ID_TYPE, Serializable.class);
	}

	@Override
	public String makeList() {
		return this.get(MAKE_LIST);
	}

	@Override
	public String lazyProperties() {
		return this.get(LAZY_PROPERTIES);
	}

	@Override
	public String persistenceManagerQualifier() {
		return this.get(PERSISTENCE_FACTORY_QUALIFIER);
	}

	@Override
	public Class<?> persistenceProvider() {
		return (Class<?>) this.get(BeanMetaData.PERSISTENCE_PROVIDER, Class.class);
	}

	// public abstract Class<? extends Annotation> annotationType();

	// public abstract boolean onlyOne();

	@Override
	public String toString() {
		return "BeanMetaData [targetEntity=" + targetEntity + ", name()=" + name() + ", scope()=" + scope() + ", targetEntity()="
				+ targetEntity() + "]";
	}

	public abstract String getBeanNameSuffix();

}