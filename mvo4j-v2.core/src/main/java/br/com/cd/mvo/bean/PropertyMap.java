package br.com.cd.mvo.bean;

public interface PropertyMap {

	public String get(String key);

	public String get(String key, String defaultValue);

	public <T> T get(String key, Class<T> returnType);

	public <T> T get(String key, Class<T> returnType, T defaultValue);

	int getInt(String key);

	int getInt(String key, int defaultValue);

	boolean getBoolean(String key);

	boolean getBoolean(String key, boolean defaultValue);

	double getDouble(String key);

	double getDouble(String key, double defaultValue);

	<T> Class<T> getAsType(String key, Class<T> returnType);

	<T> Class<T> getAsType(String key, Class<T> returnType,
			Class<T> defaultValue);
}