package br.com.cd.mvo.bean.config;

public abstract class BeanMetaDataDelegate<T> implements BeanMetaData<T> {

	protected final MetaData delegate;

	public BeanMetaDataDelegate(MetaData delegate) {
		this.delegate = delegate;
	}

	@Override
	public String get(String key) {
		return this.delegate.get(key);
	}

	@Override
	public String get(String key, String defaultValue) {
		return this.delegate.get(key, defaultValue);
	}

	@Override
	public <R> R get(String key, Class<R> returnType) {
		return this.delegate.get(key, returnType);
	}

	@Override
	public <R> R get(String key, Class<R> returnType, R defaultValue) {
		return this.delegate.get(key, returnType, defaultValue);
	}

	@Override
	public <R> Class<R> getAsType(String key, Class<R> returnType) {
		return this.delegate.getAsType(key, returnType);
	}

	@Override
	public <R> Class<R> getAsType(String key, Class<R> returnType, Class<R> defaultValue) {
		return this.delegate.getAsType(key, returnType, defaultValue);
	}

	@Override
	public int getInt(String key) {
		return this.delegate.getInt(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return this.delegate.getInt(key, defaultValue);
	}

	@Override
	public boolean getBoolean(String key) {
		return this.delegate.getBoolean(key);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return this.delegate.getBoolean(key, defaultValue);
	}

	@Override
	public double getDouble(String key) {
		return this.delegate.getDouble(key);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return this.delegate.getDouble(key, defaultValue);
	}

}