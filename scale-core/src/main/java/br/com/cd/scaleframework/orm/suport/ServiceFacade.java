package br.com.cd.scaleframework.orm.suport;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cd.scaleframework.orm.Critirion;
import br.com.cd.scaleframework.orm.OrderBy;
import br.com.cd.scaleframework.orm.Service;

public abstract class ServiceFacade<T, ID extends Serializable> implements
		Service<T, ID> {

	@Override
	public void save(T entity) {
		getRepository().save(entity);
	}

	@Override
	public void update(T entity) {
		getRepository().update(entity);
	}

	@Override
	public void delete(T entity) {
		getRepository().delete(entity);
	}

	@Override
	public T find(ID id) {
		return getRepository().find(id);
	}

	@Override
	public T find(String propertyName, Object value) {
		return getRepository().find(propertyName, value);
	}

	@Override
	public T find(Map<String, Object> map) {
		return getRepository().find(map);
	}

	@Override
	public T find(String[] propertyNames, Object[] values) {
		return getRepository().find(propertyNames, values);
	}

	@Override
	public T find(String associationPath, String propertyName, Object value) {
		return getRepository().find(associationPath, propertyName, value);
	}

	@Override
	public List<T> findList() {
		return getRepository().findList();
	}

	@Override
	public List<T> findList(String orderBy) {
		return getRepository().findList(orderBy);
	}

	@Override
	public List<T> findList(String orderBy, OrderBy orderByType) {
		return getRepository().findList(orderBy, orderByType);
	}

	@Override
	public List<T> findList(Integer firstResult, Integer maxResults) {
		return getRepository().findList(firstResult, maxResults);
	}

	@Override
	public List<T> findList(String orderBy, Integer firstResult,
			Integer maxResults) {
		return getRepository().findList(orderBy, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String orderBy, OrderBy orderByType,
			Integer firstResult, Integer maxResults) {
		return getRepository().findList(orderBy, orderByType, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value) {
		return getRepository().findList(associationPath, propertyName, value);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Integer firstResult, Integer maxResults) {
		return getRepository().findList(associationPath, propertyName, value,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value) {
		return getRepository().findList(propertyName, value);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values) {
		return getRepository().findList(propertyNames, values);
	}

	@Override
	public List<T> findList(Map<String, Object> map) {
		return getRepository().findList(map);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults) {
		return getRepository().findList(map, firstResult, maxResults);
	}

	@Override
	public Long getListCount() {
		return getRepository().getListCount();
	}

	@Override
	public T find(String propertyName, Object value, Critirion likeType) {
		return getRepository().find(propertyName, value, likeType);
	}

	@Override
	public T find(Map<String, Object> map, Critirion likeType) {
		return getRepository().find(map, likeType);
	}

	@Override
	public T findLikeMap(Map<String, Entry<Object, Critirion>> map) {
		return getRepository().findLikeMap(map);
	}

	@Override
	public T find(String[] propertyNames, Object[] values, Critirion likeType) {
		return getRepository().find(propertyNames, values, likeType);
	}

	@Override
	public T find(String associationPath, String propertyName, Object value,
			Critirion likeType) {
		return getRepository().find(associationPath, propertyName, value,
				likeType);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Critirion likeType) {
		return getRepository().findList(associationPath, propertyName, value,
				likeType);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return getRepository().findList(associationPath, propertyName, value,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Critirion likeType) {
		return getRepository().findList(propertyName, value, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Integer firstResult, Integer maxResults) {
		return getRepository().findList(propertyName, value, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return getRepository().findList(propertyName, value, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType) {
		return getRepository().findList(propertyName, value, orderBy,
				orderByType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Critirion likeType) {
		return getRepository().findList(propertyName, value, orderBy,
				orderByType, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy) {
		return getRepository().findList(propertyName, value, orderBy);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Critirion likeType) {
		return getRepository().findList(propertyName, value, orderBy, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Integer firstResult, Integer maxResults) {
		return getRepository().findList(propertyName, value, orderBy,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return getRepository().findList(propertyName, value, orderBy,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults) {
		return getRepository().findList(propertyName, value, orderBy,
				orderByType, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return getRepository().findList(propertyName, value, orderBy,
				orderByType, firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Critirion likeType) {
		return getRepository().findList(propertyNames, values, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType) {
		return getRepository().findList(propertyNames, values, orderBy,
				orderByType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Critirion likeType) {
		return getRepository().findList(propertyNames, values, orderBy,
				orderByType, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy) {
		return getRepository().findList(propertyNames, values, orderBy);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Critirion likeType) {
		return getRepository().findList(propertyNames, values, orderBy,
				likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Integer firstResult, Integer maxResults) {
		return getRepository().findList(propertyNames, values, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Integer firstResult, Integer maxResults, Critirion likeType) {
		return getRepository().findList(propertyNames, values, firstResult,
				maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Integer firstResult, Integer maxResults) {
		return getRepository().findList(propertyNames, values, orderBy,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Integer firstResult, Integer maxResults,
			Critirion likeType) {
		return getRepository().findList(propertyNames, values, orderBy,
				firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults) {
		return getRepository().findList(propertyNames, values, orderBy,
				orderByType, firstResult, maxResults);
	}

	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults, Critirion likeType) {

		return getRepository().findList(propertyNames, values, orderBy,
				orderByType, firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Critirion likeType) {
		return getRepository().findList(map, likeType);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map) {
		return getRepository().findListLikeMap(map);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			Integer firstResult, Integer maxResults) {
		return getRepository().findListLikeMap(map, firstResult, maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, Critirion likeType) {
		return getRepository().findList(map, firstResult, maxResults, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType) {
		return getRepository().findList(map, orderBy, orderByType);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType) {

		return getRepository().findListLikeMap(map, orderBy, orderByType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Critirion likeType) {

		return getRepository().findList(map, orderBy, orderByType, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy) {

		return getRepository().findList(map, orderBy);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy) {

		return getRepository().findListLikeMap(map, orderBy);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Critirion likeType) {

		return getRepository().findList(map, orderBy, likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults) {

		return getRepository().findList(map, orderBy, firstResult, maxResults);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, Integer firstResult, Integer maxResults) {

		return getRepository().findListLikeMap(map, orderBy, firstResult,
				maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults, Critirion likeType) {

		return getRepository().findList(map, orderBy, firstResult, maxResults,
				likeType);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults) {

		return getRepository().findList(map, orderBy, orderByType, firstResult,
				maxResults);
	}

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults) {

		return getRepository().findListLikeMap(map, orderBy, orderByType,
				firstResult, maxResults);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			Critirion likeType) {

		return getRepository().findList(map, orderBy, orderByType, firstResult,
				maxResults, likeType);
	}

	@Override
	public T findByQuery(String queryString,
			Entry<String, Object>... parameters) {

		return getRepository().findByQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByQuery(String queryString,
			Entry<String, Object>... parameters) {

		return getRepository().findListByQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByQuery(String queryString, Integer firstResult,
			Integer maxResults, Entry<String, Object>... parameters) {

		return getRepository().findListByQuery(queryString, firstResult,
				maxResults, parameters);
	}

	@Override
	public T findByNativeQuery(String queryString,
			Entry<String, Object>... parameters) {

		return getRepository().findByNativeQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByNativeQuery(String queryString,
			Entry<String, Object>... parameters) {

		return getRepository().findListByNativeQuery(queryString, parameters);
	}

	@Override
	public List<T> findListByNativeQuery(String queryString,
			Integer firstResult, Integer maxResults,
			Entry<String, Object>... parameters) {

		return getRepository().findListByNativeQuery(queryString, firstResult,
				maxResults, parameters);
	}

	@Override
	public Long getListCount(String propertyName, Object value) {

		return getRepository().getListCount(propertyName, value);
	}

	@Override
	public Long getListCount(String propertyName, Object value,
			Critirion likeType) {

		return getRepository().getListCount(propertyName, value, likeType);
	}

	@Override
	public Long getListCount(String[] propertyNames, Object[] values) {

		return getRepository().getListCount(propertyNames, values);
	}

	@Override
	public Long getListCount(String[] propertyNames, Object[] values,
			Critirion likeType) {

		return getRepository().getListCount(propertyNames, values, likeType);
	}

	@Override
	public Long getListCount(Map<String, Object> map) {

		return getRepository().getListCount(map);
	}

	@Override
	public Long getListCount(Map<String, Object> map, Critirion likeType) {

		return getRepository().getListCount(map, likeType);
	}

	@Override
	public Long getListCountLikeMap(Map<String, Entry<Object, Critirion>> map) {

		return getRepository().getListCountLikeMap(map);
	}

}