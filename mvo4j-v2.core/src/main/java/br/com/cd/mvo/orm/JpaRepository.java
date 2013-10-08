package br.com.cd.mvo.orm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public interface JpaRepository<T> extends SQLRepository<T> {

	EntityManager getEntityManager();

	T find(final CriteriaBuilder cb);

	T find(final CriteriaBuilder cb, final OrderBy orderBy);

	List<T> findList(final CriteriaBuilder cb);

	List<T> findList(final CriteriaBuilder cb, final Integer firstResult,
			Integer maxResults);

	List<T> findList(final CriteriaBuilder cb, final OrderBy orderBy);

	List<T> findList(final CriteriaBuilder cb, final OrderBy orderBy,
			final Integer firstResult, final Integer maxResults);

	Long getListCount(final CriteriaBuilder cb);

	CriteriaQuery<T> createCriteriaQuery();

	CriteriaQuery<T> createCriteriaQuery(final CriteriaBuilder cb);

	CriteriaQuery<T> createCriteriaQuery(final OrderBy orderBy);

	CriteriaQuery<T> createCriteriaQuery(final CriteriaBuilder cb,
			OrderBy orderBy);
}