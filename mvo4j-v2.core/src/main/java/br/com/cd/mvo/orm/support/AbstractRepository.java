package br.com.cd.mvo.orm.support;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.ioc.scan.NoScan;
import br.com.cd.mvo.orm.LikeCritirion;
import br.com.cd.mvo.orm.LikeCritirionEnum;
import br.com.cd.mvo.orm.ListenableRepository;
import br.com.cd.mvo.orm.OrderBy;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.RepositoryListener;
import br.com.cd.mvo.util.ThreadLocalMapUtil;

public abstract class AbstractRepository<T> implements ListenableRepository<T> {

	@NoScan
	protected RepositoryListener<T> listener = new RepositoryListener<T>() {

		@Override
		public void onRead(List<T> entity) {
		}

		@Override
		public boolean onSave(T entity,
				RepositoryListener.ActionListenerEventType eventType) {
			return true;
		}

		@Override
		public boolean onUpdate(T entity,
				RepositoryListener.ActionListenerEventType eventType) {
			return true;
		}

		@Override
		public boolean onDelete(T entity,
				RepositoryListener.ActionListenerEventType eventType) {
			return true;
		}

		@Override
		public void postConstruct(Repository<T> repository) {
		}

		@Override
		public void preDestroy(Repository<T> repository) {
		}
	};

	protected final Class<T> entityClass;

	protected RepositoryMetaData metaData;

	public AbstractRepository(Class<T> entityClass, RepositoryMetaData metaData) {
		this.entityClass = entityClass;
		this.metaData = metaData;
	}

	@Override
	public final void setListener(RepositoryListener<T> listener) {
		this.listener = listener;
	}

	@Override
	public RepositoryListener<T> getListener() {
		return listener;
	}

	@PostConstruct
	@Override
	public void afterPropertiesSet() {
		listener.postConstruct(this);
	}

	@PreDestroy
	@Override
	public final void destroy() {
		listener.preDestroy(this);
	}

	@Override
	public RepositoryMetaData getBeanMetaData() {
		return metaData;
	}

	protected abstract T doSave(final T entity);

	@Override
	public final T save(final T entity) {
		T result = entity;
		if (listener.onSave(entity,
				RepositoryListener.ActionListenerEventType.BEFORE)) {
			result = doSave(entity);
			listener.onSave(entity,
					RepositoryListener.ActionListenerEventType.AFTER);
		}
		return result;
	}

	protected abstract T doUpdate(final T entity);

	@Override
	public final T update(T entity) {
		if (listener.onUpdate(entity,
				RepositoryListener.ActionListenerEventType.BEFORE)) {
			entity = doUpdate(entity);
			listener.onUpdate(entity,
					RepositoryListener.ActionListenerEventType.AFTER);
		}
		return entity;
	}

	protected abstract void doDelete(final T entity);

	@Override
	public final void delete(final T entity) {
		if (listener.onDelete(entity,
				RepositoryListener.ActionListenerEventType.BEFORE)) {
			doDelete(entity);
			listener.onDelete(entity,
					RepositoryListener.ActionListenerEventType.AFTER);
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

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = new LinkedHashMap<>();

		Object threadVariable = ThreadLocalMapUtil
				.getThreadVariable("PARAMETER_ANNOTATIONS");

		Annotation[] annotations = new Annotation[0];
		if (threadVariable != null)
			annotations = (Annotation[]) threadVariable;

		int i = -1;
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			LikeCritirionEnum critirion = likeCritiria != null ? likeCritiria
					: LikeCritirionEnum.NONE;
			if (annotations.length > ++i)
				if (annotations[i] instanceof LikeCritirion)
					critirion = ((LikeCritirion) annotations[i]).value();

			Entry<Object, LikeCritirionEnum> newValue = new AbstractMap.SimpleEntry<>(
					value, critirion);
			newMap.put(key, newValue);
		}

		return newMap;
	}

	protected final Map<String, Object> entriesToMap(
			Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(parameter.getKey(), parameter.getValue());

		for (Entry<String, Object> entry : parameters) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

}
