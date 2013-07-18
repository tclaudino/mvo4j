package br.com.cd.scaleframework.orm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface Repository<T, ID extends Serializable> {

	void save(T entity);

	void update(T entity);

	void delete(T entity);

	T find(ID id);

	T find(String propertyName, Object value);

	T find(String propertyName, Object value, Critirion likeType);

	T find(final Map<String, Object> map);

	T find(final Map<String, Object> map, Critirion likeType);

	T findLikeMap(final Map<String, Entry<Object, Critirion>> map);

	T find(String[] propertyNames, Object[] values);

	T find(String[] propertyNames, Object[] values, Critirion likeType);

	T find(String associationPath, String propertyName, Object value);

	T find(String associationPath, String propertyName, Object value,
			Critirion likeType);

	List<T> findList();

	List<T> findList(String orderBy);

	List<T> findList(String orderBy, OrderBy orderByType);

	List<T> findList(Integer firstResult, Integer maxResults);

	List<T> findList(String orderBy, Integer firstResult, Integer maxResults);

	List<T> findList(String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults);

	List<T> findList(String associationPath, String propertyName, Object value);

	List<T> findList(String associationPath, String propertyName, Object value,
			Critirion likeType);

	List<T> findList(String associationPath, String propertyName, Object value,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String associationPath, String propertyName, Object value,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(String propertyName, Object value);

	List<T> findList(String propertyName, Object value, Critirion likeType);

	List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, Critirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy);

	List<T> findList(String propertyName, Object value, final String orderBy,
			Critirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults, Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values);

	List<T> findList(String[] propertyNames, Object[] values, Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType, Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final Integer firstResult,
			final Integer maxResults, Critirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(Map<String, Object> map);

	List<T> findList(Map<String, Object> map, Critirion likeType);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map);

	List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			Integer firstResult, Integer maxResults);

	List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, Critirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			final String orderBy, final OrderBy orderByType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, Critirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			final String orderBy);

	List<T> findList(Map<String, Object> map, final String orderBy,
			Critirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final Integer firstResult, final Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			final String orderBy, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final Integer firstResult, final Integer maxResults,
			Critirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults, Critirion likeType);

	T findByQuery(final String queryString,
			final Entry<String, Object>... parameters);

	List<T> findListByQuery(final String queryString,
			final Entry<String, Object>... parameters);

	List<T> findListByQuery(final String queryString,
			final Integer firstResult, final Integer maxResults,
			final Entry<String, Object>... parameters);

	T findByNativeQuery(final String queryString,
			final Entry<String, Object>... parameters);

	List<T> findListByNativeQuery(final String queryString,
			final Entry<String, Object>... parameters);

	List<T> findListByNativeQuery(final String queryString,
			final Integer firstResult, final Integer maxResults,
			final Entry<String, Object>... parameters);

	Long getListCount();

	Long getListCount(String propertyName, Object value);

	Long getListCount(String propertyName, Object value, Critirion likeType);

	Long getListCount(String[] propertyNames, Object[] values);

	Long getListCount(String[] propertyNames, Object[] values,
			Critirion likeType);

	Long getListCount(Map<String, Object> map);

	Long getListCount(Map<String, Object> map, Critirion likeType);

	Long getListCountLikeMap(Map<String, Entry<Object, Critirion>> map);

}
