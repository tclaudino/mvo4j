package br.com.cd.mvo.core;

import java.util.Collection;
import java.util.Map;

import br.com.cd.mvo.orm.LikeCritirionEnum;

public interface FilterManager {

	public class FilterField {

		private final String fieldName;
		private Object searchValue;
		private final LikeCritirionEnum likeType;

		public FilterField(String fieldName, LikeCritirionEnum likeType) {
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

		public LikeCritirionEnum getLikeType() {
			return likeType;
		}
	}

	FilterManager setValue(String fieldName, Object value);

	FilterManager setValue(String fieldName, Object value, LikeCritirionEnum likeType);

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