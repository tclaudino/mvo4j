package br.com.cd.mvo.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cd.mvo.core.BeanObject;

public interface Repository<T, D> extends BeanObject<T> {

	D getDelegate();

	T save(final T entity);

	T update(T entity);

	void delete(final T entity);

	T find(final Serializable id);

	T find(final String propertyName, final Object value);

	T find(final Map<String, Object> map);

	T find(final Map<String, Object> map, final LikeCritirionEnum likeCritiria);

	T find(final Entry<String, Object> parameter, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	Collection<T> findList();

	Collection<T> findList(final OrderBy orderBy);

	Collection<T> findList(final Integer firstResult, final Integer maxResults);

	Collection<T> findList(final Integer firstResult, final Integer maxResults, final OrderBy orderBy);

	Collection<T> findList(final Map<String, Object> map);

	Collection<T> findList(final Map<String, Object> map, final OrderBy orderBy);

	Collection<T> findList(final Map<String, Object> map, final Integer firstResult, Integer maxResults);

	Collection<T> findList(final Map<String, Object> map, final Integer firstResult, Integer maxResults, final OrderBy orderBy);

	Collection<T> findList(final Map<String, Object> map, final LikeCritirionEnum likeCritiria);

	Collection<T> findList(final Map<String, Object> map, final LikeCritirionEnum likeCritiria, OrderBy orderBy);

	Collection<T> findList(final Map<String, Object> map, final LikeCritirionEnum likeCritiria, Integer firstResult,
			final Integer maxResults);

	Collection<T> findList(final Map<String, Object> map, final LikeCritirionEnum likeCritiria, Integer firstResult,
			final Integer maxResults, final OrderBy orderBy);

	Collection<T> findList(final Entry<String, Object> parameter, @SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Collection<T> findList(final OrderBy orderBy, final Entry<String, Object> parameter,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Collection<T> findList(final Integer firstResult, final Integer maxResults, Entry<String, Object> parameter,
			final @SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Collection<T> findList(final Integer firstResult, final Integer maxResults, final OrderBy orderBy, Entry<String, Object> parameter,
			final @SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Long getListCount();

	Long getListCount(final String propertyName, final Object value);

	Long getListCount(final Map<String, Object> map);

	Long getListCount(final Map<String, Object> map, final LikeCritirionEnum likeCritiria);

	Long getListCount(final Entry<String, Object> parameter, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters);
}
