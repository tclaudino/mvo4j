package br.com.cd.mvo.orm;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractRepository<T> implements Repository<T> {

	protected final Class<T> entityClass;
	protected Repository.ActionListener<T> listener = new Repository.ActionListener<T>() {

		@Override
		public void onRead(List<T> entity) {
		}

		@Override
		public boolean onSave(T entity,
				Repository.ActionListenerEventType eventType) {
			return false;
		}

		@Override
		public boolean onUpdate(T entity,
				Repository.ActionListenerEventType eventType) {
			return false;
		}

		@Override
		public boolean onDelete(T entity,
				Repository.ActionListenerEventType eventType) {
			return false;
		}
	};

	public AbstractRepository(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public final void setListener(Repository.ActionListener<T> listener) {
		this.listener = listener;
	}

	protected abstract T doSave(final T entity);

	@Override
	public final T save(final T entity) {
		T result = entity;
		if (listener.onSave(entity, Repository.ActionListenerEventType.BEFORE)) {
			result = doSave(entity);
			listener.onSave(entity, Repository.ActionListenerEventType.AFTER);
		}
		return result;
	}

	protected abstract T doUpdate(final T entity);

	@Override
	public final T update(T entity) {
		if (listener
				.onUpdate(entity, Repository.ActionListenerEventType.BEFORE)) {
			entity = doUpdate(entity);
			listener.onUpdate(entity, Repository.ActionListenerEventType.AFTER);
		}
		return entity;
	}

	protected abstract void doDelete(final T entity);

	@Override
	public final void delete(final T entity) {
		if (listener
				.onDelete(entity, Repository.ActionListenerEventType.BEFORE)) {
			doDelete(entity);
			listener.onDelete(entity, Repository.ActionListenerEventType.AFTER);
		}
	}

	protected abstract T doFind(final Serializable id);

	@Override
	public final T find(final Serializable id) {
		T toResult = doFind(id);
		if (toResult != null) {
			ArrayList<T> list = new ArrayList<>();
			list.add(toResult);
			listener.onRead(list);
		}
		return toResult;
	}

	@Override
	public final T find(String propertyName, Object value) {

		Map<String, Object> map = new HashMap<>();
		map.put(propertyName, value);
		return find(map);
	}

	@Override
	public final T find(Map<String, Object> map) {

		return this.find(map, LikeCritirionEnum.NONE);
	}

	@Override
	public final T find(Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.find(entriesToMap(parameter, parameters));
	}

	@Override
	public final List<T> findList() {

		return this.findList((OrderBy) null);
	}

	@Override
	public final List<T> findList(OrderBy orderBy) {

		return this.findList(-1, -1, orderBy);
	}

	@Override
	public final List<T> findList(Integer firstResult, Integer maxResults) {

		return this.findList(-1, -1, null);
	}

	@Override
	public final List<T> findList(Map<String, Object> map) {

		return this.findList(map, (OrderBy) null);
	}

	@Override
	public final List<T> findList(Map<String, Object> map, OrderBy orderBy) {

		return findList(map, -1, -1, orderBy);
	}

	@Override
	public final List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults) {

		return this.findList(map, firstResult, maxResults, null);
	}

	@Override
	public final List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, OrderBy orderBy) {

		return this.findList(map, LikeCritirionEnum.NONE, firstResult,
				maxResults, orderBy);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			LikeCritirionEnum likeCritiria) {

		return this.findList(map, likeCritiria, null);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			LikeCritirionEnum likeCritiria, OrderBy orderBy) {

		return this.findList(map, likeCritiria, -1, -1, orderBy);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			LikeCritirionEnum likeCritiria, Integer firstResult,
			Integer maxResults) {

		return this.findList(map, likeCritiria, firstResult, maxResults, null);
	}

	@Override
	public final List<T> findList(Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters));
	}

	@Override
	public final List<T> findList(OrderBy orderBy,
			Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), orderBy);
	}

	@Override
	public final List<T> findList(Integer firstResult, Integer maxResults,
			Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), firstResult,
				maxResults);
	}

	@Override
	public final List<T> findList(Integer firstResult, Integer maxResults,
			OrderBy orderBy, Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), firstResult,
				maxResults, orderBy);
	}

	@Override
	public final Long getListCount(String propertyName, Object value) {

		Map<String, Object> map = new HashMap<>();
		map.put(propertyName, value);
		return getListCount(map);
	}

	@Override
	public final Long getListCount(Map<String, Object> map) {

		return this.getListCount(map, LikeCritirionEnum.NONE);
	}

	@Override
	public final Long getListCount(Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.getListCount(entriesToMap(parameter, parameters));
	}

	protected final Map<String, Entry<Object, LikeCritirionEnum>> applyLikeMap(
			Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = new HashMap<>();

		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			LikeCritirionEnum likeCritirionEnum = likeCritiria;
			LikeCritirion annotation = key.getClass().getAnnotation(
					LikeCritirion.class);
			if (annotation == null) {
				annotation = value.getClass()
						.getAnnotation(LikeCritirion.class);
			}
			if (annotation != null) {
				likeCritirionEnum = annotation.value();
			}
			Entry<Object, LikeCritirionEnum> newValue = new AbstractMap.SimpleEntry<>(
					value, likeCritirionEnum);
			newMap.put(key, newValue);
		}

		return newMap;
	}

	protected final Map<String, Object> entriesToMap(
			Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		Map<String, Object> map = new HashMap<>();
		map.put(parameter.getKey(), parameter.getValue());

		for (Entry<String, Object> entry : parameters) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

}
