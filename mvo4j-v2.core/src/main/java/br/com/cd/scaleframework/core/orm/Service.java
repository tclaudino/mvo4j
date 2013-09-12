package br.com.cd.scaleframework.core.orm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface Service<T, ID extends Serializable> {

	public interface ActionListener<T> {

		void onRead(T entity);

		void onRead(List<T> entity);

		boolean beforeSave(T entity);

		boolean afterSave(T entity);

		boolean beforeUpdate(T entity);

		boolean afterUpdate(T entity);

		boolean beforeDelete(T entity);

		boolean afterDelete(T entity);
	}

	void setListener(ActionListener<T> listener);

	void save(T entity);

	void update(T entity);

	void delete(T entity);

	T find(ID id);

	T find(String propertyName, Object value);

	T find(String propertyName, Object value, LikeCritirion likeType);

	T find(final Map<String, Object> map);

	T find(final Map<String, Object> map, LikeCritirion likeType);

	T findLikeMap(final Map<String, Entry<Object, LikeCritirion>> map);

	T find(String[] propertyNames, Object[] values);

	T find(String[] propertyNames, Object[] values, LikeCritirion likeType);

	T find(String associationPath, String propertyName, Object value);

	T find(String associationPath, String propertyName, Object value,
			LikeCritirion likeType);

	List<T> findList();

	List<T> findList(String orderBy);

	List<T> findList(String orderBy, OrderBy orderByType);

	List<T> findList(Integer firstResult, Integer maxResults);

	List<T> findList(String orderBy, Integer firstResult, Integer maxResults);

	List<T> findList(String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults);

	List<T> findList(String associationPath, String propertyName, Object value);

	List<T> findList(String associationPath, String propertyName, Object value,
			LikeCritirion likeType);

	List<T> findList(String associationPath, String propertyName, Object value,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String associationPath, String propertyName, Object value,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(String propertyName, Object value);

	List<T> findList(String propertyName, Object value, LikeCritirion likeType);

	List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, LikeCritirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy);

	List<T> findList(String propertyName, Object value, final String orderBy,
			LikeCritirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(String propertyName, Object value, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults, LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values);

	List<T> findList(String[] propertyNames, Object[] values,
			LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final Integer firstResult,
			final Integer maxResults, LikeCritirion likeType);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(Map<String, Object> map);

	List<T> findList(Map<String, Object> map, LikeCritirion likeType);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map);

	List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map,
			Integer firstResult, Integer maxResults);

	List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, LikeCritirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map,
			final String orderBy, final OrderBy orderByType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, LikeCritirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map,
			final String orderBy);

	List<T> findList(Map<String, Object> map, final String orderBy,
			LikeCritirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final Integer firstResult, final Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map,
			final String orderBy, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults);

	List<T> findList(Map<String, Object> map, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults, LikeCritirion likeType);

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

	Long getListCount(String propertyName, Object value, LikeCritirion likeType);

	Long getListCount(String[] propertyNames, Object[] values);

	Long getListCount(String[] propertyNames, Object[] values,
			LikeCritirion likeType);

	Long getListCount(Map<String, Object> map);

	Long getListCount(Map<String, Object> map, LikeCritirion likeType);

	Long getListCountLikeMap(Map<String, Entry<Object, LikeCritirion>> map);

}
