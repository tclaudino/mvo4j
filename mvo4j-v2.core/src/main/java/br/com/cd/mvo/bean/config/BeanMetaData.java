package br.com.cd.mvo.bean.config;

import java.io.Serializable;

public interface BeanMetaData<T> extends MetaData {

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

	public String get(String key);

	public String get(String key, String defaultValue);

	public <R> R get(String key, Class<R> returnType);

	public <R> R get(String key, Class<R> returnType, R defaultValue);

	<R> Class<R> getAsType(String key, Class<R> returnType);

	<R> Class<R> getAsType(String key, Class<R> returnType, Class<R> defaultValue);

	int getInt(String key);

	int getInt(String key, int defaultValue);

	boolean getBoolean(String key);

	boolean getBoolean(String key, boolean defaultValue);

	double getDouble(String key);

	double getDouble(String key, double defaultValue);

	Class<?> persistenceProvider();

	String persistenceManagerQualifier();

	String lazyProperties();

	String makeList();

	Class<Serializable> entityIdType();

	Class<T> targetEntity();

	String messageBundle();

	int initialPageSize();

	String scope();

	String name();
}