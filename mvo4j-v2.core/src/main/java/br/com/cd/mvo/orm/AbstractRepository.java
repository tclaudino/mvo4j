package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.Listenable;
import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.util.ThreadLocalMapUtil;

@SuppressWarnings("rawtypes")
public abstract class AbstractRepository<T, D> implements Repository<T, D>, Listenable<RepositoryListener> {

	protected final Class<T> entityClass;

	protected RepositoryMetaData<T> metaData;

	public AbstractRepository(RepositoryMetaData<T> metaData) {
		this.entityClass = metaData.targetEntity();
		this.metaData = metaData;
	}

	@Override
	public BeanMetaData<T> getBeanMetaData() {
		return metaData;
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
	public final T find(Entry<String, Object> parameter, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.find(entriesToMap(parameter, parameters));
	}

	@Override
	public final Collection<T> findList() {

		return this.findList((OrderBy) null);
	}

	@Override
	public final Collection<T> findList(OrderBy orderBy) {

		return this.findList(-1, -1, orderBy);
	}

	@Override
	public final Collection<T> findList(Integer firstResult, Integer maxResults) {

		return this.findList(-1, -1, null);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map) {

		return this.findList(map, (OrderBy) null);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, OrderBy orderBy) {

		return findList(map, -1, -1, orderBy);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, Integer firstResult, Integer maxResults) {

		return this.findList(map, firstResult, maxResults, null);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, Integer firstResult, Integer maxResults, OrderBy orderBy) {

		return this.findList(map, LikeCritirionEnum.NONE, firstResult, maxResults, orderBy);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		return this.findList(map, likeCritiria, null);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria, OrderBy orderBy) {

		return this.findList(map, likeCritiria, -1, -1, orderBy);
	}

	@Override
	public final Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria, Integer firstResult, Integer maxResults) {

		return this.findList(map, likeCritiria, firstResult, maxResults, null);
	}

	@Override
	public final Collection<T> findList(Entry<String, Object> parameter, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters));
	}

	@Override
	public final Collection<T> findList(OrderBy orderBy, Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), orderBy);
	}

	@Override
	public final Collection<T> findList(Integer firstResult, Integer maxResults, Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), firstResult, maxResults);
	}

	@Override
	public final Collection<T> findList(Integer firstResult, Integer maxResults, OrderBy orderBy, Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findList(entriesToMap(parameter, parameters), firstResult, maxResults, orderBy);
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
	public final Long getListCount(Entry<String, Object> parameter, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.getListCount(entriesToMap(parameter, parameters));
	}

	protected final Map<String, Entry<Object, LikeCritirionEnum>> applyLikeMap(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = new LinkedHashMap<>();

		Object threadVariable = ThreadLocalMapUtil.getThreadVariable("PARAMETER_ANNOTATIONS");

		Annotation[] annotations = new Annotation[0];
		if (threadVariable != null)
			annotations = (Annotation[]) threadVariable;

		int i = -1;
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			LikeCritirionEnum critirion = likeCritiria != null ? likeCritiria : LikeCritirionEnum.NONE;
			if (annotations.length > ++i)
				if (annotations[i] instanceof LikeCritirion)
					critirion = ((LikeCritirion) annotations[i]).value();

			Entry<Object, LikeCritirionEnum> newValue = new AbstractMap.SimpleEntry<>(value, critirion);
			newMap.put(key, newValue);
		}

		return newMap;
	}

	protected final Map<String, Object> entriesToMap(Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		Map<String, Object> map = new LinkedHashMap<>();
		map.put(parameter.getKey(), parameter.getValue());

		for (Entry<String, Object> entry : parameters) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	@PostConstruct
	@Override
	public void postConstruct() {
		// only proxy listener
	}

	@PreDestroy
	@Override
	public final void preDestroy() {
		// only proxy listener
	}

	@Override
	public Class<RepositoryListener> getListenerType() {
		return RepositoryListener.class;
	}

}
