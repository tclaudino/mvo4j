package br.com.cd.scaleframework.controller;

import java.util.Collection;
import java.util.Map;

import br.com.cd.scaleframework.core.orm.LikeCritirion;

public interface FilterManager {

	public class FilterField {

		private final String fieldName;
		private Object searchValue;
		private final LikeCritirion likeType;

		public FilterField(String fieldName, LikeCritirion likeType) {
			this.fieldName = fieldName;
			this.likeType = likeType;
		}

		public Object getSearchValue() {
			return searchValue;
		}

		public void setSearchValue(Object searchValue) {
			this.searchValue = searchValue;
		}

		public String getFieldName() {
			return fieldName;
		}

		public LikeCritirion getLikeType() {
			return likeType;
		}
	}

	FilterManager setValue(String fieldName, Object value);

	FilterManager setValue(String fieldName, Object value,
			LikeCritirion likeType);

	Map<String, Object> getFilterMap();

	void applyFilter();

	Collection<FilterField> getSelectedFieldList();

	void selectField(String fieldName);

	FilterField[] getSelectedFields();

	Collection<FilterField> getFieldList();

	FilterField[] getFields();

	FilterManager unSelectField(String fieldName);

	FilterManager removeField(String fieldName);
}