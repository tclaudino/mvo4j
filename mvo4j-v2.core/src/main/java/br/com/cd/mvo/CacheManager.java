package br.com.cd.mvo;

public interface CacheManager {

	boolean add(String key, Object value, long seconds);

	Object getObject(String key);

	<T> T getObject(Class<T> clazz, String key);

	java.util.List<?> getObjects(String key);

	<T> java.util.List<T> getObjects(Class<T> clazz, String key);

	long getCacheManagerMaxSize();

}