package br.com.cd.mvo.orm;

import java.util.Collection;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface JpaRepository<T> extends SQLRepository<T, EntityManager> {

	T find(final CriteriaBuilder cb);

	T find(final CriteriaBuilder cb, final OrderBy orderBy);

	Collection<T> findList(final CriteriaBuilder cb);

	Collection<T> findList(final CriteriaBuilder cb, final Integer firstResult, Integer maxResults);

	Collection<T> findList(final CriteriaBuilder cb, final OrderBy orderBy);

	Collection<T> findList(final CriteriaBuilder cb, final OrderBy orderBy, final Integer firstResult, final Integer maxResults);

	Long getListCount(final CriteriaBuilder cb);

	CriteriaQuery<T> createCriteriaQuery();

	CriteriaQuery<T> createCriteriaQuery(final CriteriaBuilder cb);

	CriteriaQuery<T> createCriteriaQuery(final OrderBy orderBy);

	CriteriaQuery<T> createCriteriaQuery(final CriteriaBuilder cb, OrderBy orderBy);

	T findByNamedQuery(String queryString, @SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Collection<T> findListByNamedQuery(String queryString, @SuppressWarnings("unchecked") Entry<String, Object>... parameters);

	Collection<T> findListByNamedQuery(String queryString, Integer firstResult, Integer maxResults,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters);
}