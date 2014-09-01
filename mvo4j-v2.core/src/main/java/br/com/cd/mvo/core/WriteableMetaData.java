package br.com.cd.mvo.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cd.util.ParserUtils;

public class WriteableMetaData implements MetaData {

	Logger looger = LoggerFactory.getLogger(BeanMetaData.class);

	private Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public String get(String key) {
		return this.get(key, "");
	}

	@Override
	public String get(String key, String defaultValue) {
		return ParserUtils.parseString(this.map.get(key), defaultValue);
	}

	@Override
	public <T> T get(String key, Class<T> returnType) {
		return this.get(key, returnType, null);
	}

	@Override
	public <T> T get(String key, Class<T> returnType, T defaultValue) {
		return ParserUtils.parseObject(returnType, this.map.get(key), defaultValue);
	}

	@Override
	public int getInt(String key) {
		return this.getInt(key, 0);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return ParserUtils.parseInt(this.map.get(key), defaultValue);
	}

	@Override
	public boolean getBoolean(String key) {
		return this.getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return ParserUtils.parseBoolean(this.map.get(key), defaultValue);
	}

	@Override
	public double getDouble(String key) {
		return this.getDouble(key, 0L);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return ParserUtils.parseDouble(this.map.get(key), defaultValue);
	}

	@Override
	public <T> Class<T> getAsType(String key, Class<T> returnType) {
		return this.getAsType(key, returnType, null);
	}

	@Override
	public <T> Class<T> getAsType(String key, Class<T> returnType, Class<T> defaultValue) {
		Object value = this.map.get(key);
		try {
			@SuppressWarnings("unchecked")
			Class<T> result = (Class<T>) value;
			return result != null ? result : defaultValue;
		} catch (Exception e) {
			looger.error("Can't convert '" + value + "' to '" + returnType + "'", e);
			return defaultValue;
		}
	}

	public void add(String key, Object value) {
		this.map.put(key, value);
	}

	public void addAll(Map<String, Object> m) {
		this.map.putAll(m);
	}
}