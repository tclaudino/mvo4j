package br.com.cd.scaleframework.context;

public interface CacheManager {

	boolean add(String key, Object value, int seconds);

	Object getObject(String key);

	<T> T getObject(Class<T> clazz, String key);

	java.util.List<?> getObjects(String key);

	<T> java.util.List<T> getObjects(Class<T> clazz, String key);

}