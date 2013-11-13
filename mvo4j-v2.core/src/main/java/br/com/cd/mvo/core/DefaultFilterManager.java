package br.com.cd.mvo.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.ApplicationKeys;
import br.com.cd.mvo.orm.LikeCritirionEnum;

public class DefaultFilterManager<T> implements FilterManager {

	private Controller<T> controller;

	private Map<String, FilterField> filterFields = new HashMap<String, FilterField>();

	private Map<String, FilterField> selectedFilterFields = new HashMap<String, FilterField>();

	public DefaultFilterManager(Controller<T> controller) {
		this.controller = controller;
	}

	@Override
	public Collection<FilterField> getFieldList() {
		return Collections.unmodifiableCollection(this.filterFields.values());
	}

	@Override
	public FilterField[] getFields() {
		return new ArrayList<FilterField>(this.getFieldList()).toArray(new FilterField[this.filterFields.size()]);
	}

	@Override
	public Collection<FilterField> getSelectedFieldList() {
		return Collections.unmodifiableCollection(this.selectedFilterFields.values());
	}

	@Override
	public FilterField[] getSelectedFields() {
		return new ArrayList<FilterField>(this.getSelectedFieldList()).toArray(new FilterField[this.selectedFilterFields.size()]);
	}

	@Override
	public void selectField(String fieldName) {
		FilterField field = filterFields.get(fieldName);
		if (field != null) this.selectedFilterFields.put(fieldName, field);
	}

	@Override
	public FilterManager unSelectField(String fieldName) {
		this.selectedFilterFields.remove(fieldName);
		return this;
	}

	public FilterManager addField(String fieldName) {
		this.addField(fieldName, LikeCritirionEnum.NONE);
		return this;
	}

	public FilterManager addField(String fieldName, LikeCritirionEnum likeType) {
		this.addField(new FilterField(fieldName, likeType));
		return this;
	}

	public FilterManager addField(FilterField filterField) {
		this.filterFields.put(filterField.getFieldName(), filterField);
		return this;
	}

	@Override
	public FilterManager removeField(String fieldName) {
		this.filterFields.remove(fieldName);
		this.selectedFilterFields.remove(fieldName);
		return this;
	}

	@Override
	public FilterManager setValue(String fieldName, Object value) {
		return this.setValue(fieldName, value, LikeCritirionEnum.NONE);
	}

	@Override
	public FilterManager setValue(String fieldName, Object value, LikeCritirionEnum likeType) {

		FilterField field = filterFields.get(fieldName);
		if (field != null) field.setSearchValue(value);
		return this;
	}

	@Override
	public Map<String, Object> getFilterMap() {

		Map<String, Object> map = new HashMap<String, Object>();
		for (FilterField field : this.selectedFilterFields.values()) {

			map.put(field.getFieldName(), field.getSearchValue());
		}
		return Collections.unmodifiableMap(map);
	}

	@Override
	public final void applyFilter() {
		boolean success = false;

		Map<String, Object> map = this.getFilterMap();

		if (!map.isEmpty()) {

			LoggerFactory.getLogger(FilterManager.class).info("applyFilter, mapFields: {0}", map);

			try {

				List<T> entityList;
				if (controller.getInitialPageSize() > -1) {
					entityList = controller.getService().getRepository().findList(map, controller.getOffset(), controller.getPageSize());
				} else {
					entityList = controller.getService().getRepository().findList(map);
				}
				controller.setEntityList(entityList);
				success = true;
			} catch (Exception e) {
				LoggerFactory.getLogger(FilterManager.class).error("applyFilter.INVALID_SEARCH, mapFields: {0}", map);
			}
		}
		if (!success) {
			controller.addTranslatedMessage(MessageLevel.WARNING, ApplicationKeys.Entity.NOT_FOUND_SUMARY,
					ApplicationKeys.Entity.NOT_FOUND_MSG, new Object[] { controller.getName() });
		}
	}

}