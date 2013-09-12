package br.com.cd.scaleframework.controller.dynamic;

public class PropertyMapAdapter implements PropertyMap {

	private PropertyMap adaptee;

	public PropertyMapAdapter(PropertyMap adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public String get(String key) {
		return this.adaptee.get(key);
	}

	@Override
	public String get(String key, String defaultValue) {
		return this.adaptee.get(key, defaultValue);
	}

	@Override
	public <T> T get(String key, Class<T> returnType) {
		return this.adaptee.get(key, returnType);
	}

	@Override
	public <T> T get(String key, Class<T> returnType, T defaultValue) {
		return this.adaptee.get(key, returnType, defaultValue);
	}

	@Override
	public <T> Class<T> getAsType(String key, Class<T> returnType) {
		return this.adaptee.getAsType(key, returnType);
	}

	@Override
	public <T> Class<T> getAsType(String key, Class<T> returnType,
			Class<T> defaultValue) {
		return this.adaptee.getAsType(key, returnType, defaultValue);
	}

	@Override
	public int getInt(String key) {
		return this.adaptee.getInt(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return this.adaptee.getInt(key, defaultValue);
	}

	@Override
	public boolean getBoolean(String key) {
		return this.adaptee.getBoolean(key);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return this.adaptee.getBoolean(key, defaultValue);
	}

	@Override
	public double getDouble(String key) {
		return this.adaptee.getDouble(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return this.adaptee.getDouble(key, defaultValue);
	}
}