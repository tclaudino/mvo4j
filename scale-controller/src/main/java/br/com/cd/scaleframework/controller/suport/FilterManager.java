package br.com.cd.scaleframework.controller.suport;

import java.util.HashMap;
import java.util.Map;

public class FilterManager {

	private Map<String, Object> filterMap = new HashMap<String, Object>();

	public FilterManager add(String fieldName, Object value) {
		filterMap.put(fieldName, value);
		return this;
	}

	public FilterManager remove(String fieldName, Object value) {
		filterMap.remove(fieldName);
		return this;
	}

	public Map<String, Object> getFilterMap() {
		return filterMap;
	}
}