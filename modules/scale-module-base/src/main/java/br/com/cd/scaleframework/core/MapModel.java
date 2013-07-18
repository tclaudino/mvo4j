package br.com.cd.scaleframework.core;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.cd.scaleframework.util.ParserUtils;

public class MapModel {

	private Map<String, Object> objects = new LinkedHashMap<String, Object>();

	public void add(String key, Object object) {
		this.objects.put(key, object);
	}

	public void add(Object object) {
		this.objects.put(object.getClass().getName(), object);
	}

	public <T> T get(Class<T> returnType, String key) {
		if (this.objects.containsKey(key)) {
			return null;
		}
		return ParserUtils.parseObject(returnType, this.objects.get(key));
	}

	public String getString(String key) {
		return ParserUtils.parseString(this.objects.get(key));
	}

	public String getString(String key, String defaultValue) {
		return ParserUtils.parseString(this.objects.get(key), defaultValue);
	}

	public boolean getBoolean(String key) {
		return ParserUtils.parseBoolean(this.objects.get(key));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return ParserUtils.parseBoolean(this.objects.get(key), defaultValue);
	}

	public Date getDate(String key) {
		return ParserUtils.parseDate(this.objects.get(key));
	}

	public Date getDate(String key, Date defaultValue) {
		return ParserUtils.parseDate(this.objects.get(key), defaultValue);
	}

	public Date getDate(String key, DateFormat dateFormat) {
		return ParserUtils.parseDate(this.objects.get(key), dateFormat);
	}

	public Date getDate(String key, DateFormat dateFormat, Date defaultValue) {
		return ParserUtils.parseDate(this.objects.get(key), dateFormat,
				defaultValue);
	}

	public double getDouble(String key) {
		return ParserUtils.parseDouble(this.objects.get(key));
	}

	public double getDouble(String key, double defaultValue) {
		return ParserUtils.parseDouble(this.objects.get(key), defaultValue);
	}

	public float getFloat(String key) {
		return ParserUtils.parseFloat(this.objects.get(key));
	}

	public float getFloat(String key, float defaultValue) {
		return ParserUtils.parseFloat(this.objects.get(key), defaultValue);
	}

	public int getInt(String key) {
		return ParserUtils.parseInt(this.objects.get(key));
	}

	public int getInt(String key, int defaultValue) {
		return ParserUtils.parseInt(this.objects.get(key), defaultValue);
	}

	public long getLong(String key) {
		return ParserUtils.parseLong(this.objects.get(key));
	}

	public long getLong(String key, long defaultValue) {
		return ParserUtils.parseLong(this.objects.get(key), defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> key) {
		return (T) this.objects.get(key.getName());
	}
}
