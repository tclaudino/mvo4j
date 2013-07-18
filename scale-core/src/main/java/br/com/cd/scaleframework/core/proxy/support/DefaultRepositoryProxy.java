package br.com.cd.scaleframework.core.proxy.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;
import br.com.cd.scaleframework.orm.Critirion;
import br.com.cd.scaleframework.orm.OrderBy;
import br.com.cd.scaleframework.orm.Repository;

public class DefaultRepositoryProxy<T> implements RepositoryProxy<T> {

	private Repository<T, Serializable> repository;

	@SuppressWarnings("rawtypes")
	private RepositoryComponent componentProxy;

	@Override
	public void setComponent(
			@SuppressWarnings("rawtypes") RepositoryComponent componentProxy) {
		this.componentProxy = componentProxy;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public RepositoryComponent getComponent() {
		if (componentProxy == null) {
			throw new UnsupportedOperationException(PROXY_NO_SETTED);
		}
		return componentProxy;
	}

	public Repository<T, Serializable> getRepository() {
		if (repository == null) {
			throw new UnsupportedOperationException(PROXY_NO_SETTED);
		}
		return repository;
	}

	@Override
	public void setRepository(Repository<T, Serializable> repository) {
		this.repository = repository;
	}

	@Override
	public void save(T entity) {
		repository.save(entity);
	}

	@Override
	public void update(T entity) {
		repository.update(entity);
	}

	@Override
	public void delete(T entity) {
		repository.delete(entity);
	}

	@Override
	public T find(Serializable id) {
		return repository.find(id);
	}

	@Override
	public T find(String propertyName, Object value) {
		return repository.find(propertyName, value);
	}

	@Override
	public T find(Map<String, Object> map) {
		return repository.find(map);
	}

	@Override
	public T find(String[] propertyNames, Object[] values) {
		return repository.find(propertyNames, values);
	}

	@Override
	public T find(String associationPath, String propertyName, Object value) {
		return repository.find(associationPath, propertyName, value);
	}

	@Override
	public List<T> findList() {
		return repository.findList();
	}

	@Override
	public List<T> findList(String orderBy) {
		return repository.findList(orderBy);
	}

	@Override
	public List<T> findList(String orderBy, OrderBy orderByType) {
		return repository.findList(orderBy, orderByType);
	}

	@Override
	public List<T> findList(Integer firstResult, Integer maxResults) {
		return repository.findList(firstResult, maxResults);
	}

	@Override
	public List<T> findList(String orderBy, Integer firstResult,
			Integer maxResults) {
		return repository.findList(orderBy, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String orderBy, OrderBy orderByType,
			Integer firstResult, Integer maxResults) {
		return repository.findList(orderBy, orderByType, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value) {
		return repository.findList(associationPath, propertyName, value);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Integer firstResult, Integer maxResults) {
		return repository.findList(associationPath, propertyName, value,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value) {
		return repository.findList(propertyName, value);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values) {
		return repository.findList(propertyNames, values);
	}

	@Override
	public List<T> findList(Map<String, Object> map) {
		return repository.findList(map);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults) {
		return repository.findList(map, firstResult, maxResults);
	}

	@Override
	public Long getListCount() {
		return repository.getListCount();
	}

	@Override
	public T find(String propertyName, Object value, Critirion likeType) {
		return repository.find(propertyName, value, likeType);
	}

	@Override
	public T find(Map<String, Object> map, Critirion likeType) {
		return repository.find(map, likeType);
	}

	@Override
	public T findLikeMap(Map<String, Entry<Object, Critirion>> map) {
		return repository.findLikeMap(map);
	}

	@Override
	public T find(String[] propertyNames, Object[] values, Critirion likeType) {
		return repository.find(propertyNames, values, likeType);
	}

	@Override
	public T find(String associationPath, String propertyName, Object value,
			Critirion likeType) {
		return repository.find(associationPath, propertyName, value, likeType);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Critirion likeType) {
		return repository.findList(associationPath, propertyName, value,
				likeType);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return repository.findList(associationPath, propertyName, value,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Critirion likeType) {
		return repository.findList(propertyName, value, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Integer firstResult, Integer maxResults) {
		return repository
				.findList(propertyName, value, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return repository.findList(propertyName, value, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType) {
		return repository.findList(propertyName, value, orderBy, orderByType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Critirion likeType) {
		return repository.findList(propertyName, value, orderBy, orderByType,
				likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy) {
		return repository.findList(propertyName, value, orderBy);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Critirion likeType) {
		return repository.findList(propertyName, value, orderBy, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Integer firstResult, Integer maxResults) {
		return repository.findList(propertyName, value, orderBy, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return repository.findList(propertyName, value, orderBy, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults) {
		return repository.findList(propertyName, value, orderBy, orderByType,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return repository.findList(propertyName, value, orderBy, orderByType,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Critirion likeType) {
		return repository.findList(propertyNames, values, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType) {
		return repository.findList(propertyNames, values, orderBy, orderByType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Critirion likeType) {
		return repository.findList(propertyNames, values, orderBy, orderByType,
				likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy) {
		return repository.findList(propertyNames, values, orderBy);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Critirion likeType) {
		return repository.findList(propertyNames, values, orderBy, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Integer firstResult, Integer maxResults) {
		return repository.findList(propertyNames, values, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return repository.findList(propertyNames, values, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Integer firstResult, Integer maxResults) {
		return repository.findList(propertyNames, values, orderBy, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return repository.findList(propertyNames, values, orderBy, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults) {
		return repository.findList(propertyNames, values, orderBy, orderByType,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults, Critirion likeType) {

		return repository.findList(propertyNames, values, orderBy, orderByType,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Critirion likeType) {
		return repository.findList(map, likeType);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map) {
		return repository.findListLikeMap(map);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			Integer firstResult, Integer maxResults) {
		return repository.findListLikeMap(map, firstResult, maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, Critirion likeType) {
		return repository.findList(map, firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType) {
		return repository.findList(map, orderBy, orderByType);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType) {

		return repository.findListLikeMap(map, orderBy, orderByType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Critirion likeType) {

		return repository.findList(map, orderBy, orderByType, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy) {

		return repository.findList(map, orderBy);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy) {

		return repository.findListLikeMap(map, orderBy);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Critirion likeType) {

		return repository.findList(map, orderBy, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults) {

		return repository.findList(map, orderBy, firstResult, maxResults);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, Integer firstResult, Integer maxResults) {

		return repository
				.findListLikeMap(map, orderBy, firstResult, maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults, Critirion likeType) {

		return repository.findList(map, orderBy, firstResult, maxResults,
				likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults) {

		return repository.findList(map, orderBy, orderByType, firstResult,
				maxResults);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults) {

		return repository.findListLikeMap(map, orderBy, orderByType,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			Critirion likeType) {

		return repository.findList(map, orderBy, orderByType, firstResult,
				maxResults, likeType);
	}

	@Override
	public T findByQuery(String queryString,
			Entry<String, Object>... parameters) {

		return repository.findByQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByQuery(String queryString,
			Entry<String, Object>... parameters) {

		return repository.findListByQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByQuery(String queryString, Integer firstResult,
			Integer maxResults, Entry<String, Object>... parameters) {

		return repository.findListByQuery(queryString, firstResult, maxResults,
				parameters);
	}

	@Override
	public T findByNativeQuery(String queryString,
			Entry<String, Object>... parameters) {

		return repository.findByNativeQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByNativeQuery(String queryString,
			Entry<String, Object>... parameters) {

		return repository.findListByNativeQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByNativeQuery(String queryString,
			Integer firstResult, Integer maxResults,
			Entry<String, Object>... parameters) {

		return repository.findListByNativeQuery(queryString, firstResult,
				maxResults, parameters);
	}

	@Override
	public Long getListCount(String propertyName, Object value) {

		return repository.getListCount(propertyName, value);
	}

	@Override
	public Long getListCount(String propertyName, Object value,
			Critirion likeType) {

		return repository.getListCount(propertyName, value, likeType);
	}

	@Override
	public Long getListCount(String[] propertyNames, Object[] values) {

		return repository.getListCount(propertyNames, values);
	}

	@Override
	public Long getListCount(String[] propertyNames, Object[] values,
			Critirion likeType) {

		return repository.getListCount(propertyNames, values, likeType);
	}

	@Override
	public Long getListCount(Map<String, Object> map) {

		return repository.getListCount(map);
	}

	@Override
	public Long getListCount(Map<String, Object> map, Critirion likeType) {

		return repository.getListCount(map, likeType);
	}

	@Override
	public Long getListCountLikeMap(Map<String, Entry<Object, Critirion>> map) {

		return repository.getListCountLikeMap(map);
	}

}