package br.com.cd.scaleframework.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.cd.model.Entity;

import br.com.cd.scaleframework.jpa.Dao;
import br.com.cd.scaleframework.jpa.OrderBy;

@SuppressWarnings("rawtypes")
public interface JpaRepository<T extends Entity, ID extends Serializable> extends
		Dao<T, ID> {

	T find(final CriteriaBuilder cb);

	T find(final CriteriaBuilder cb, Integer firstResult, Integer maxResults);

	T find(final CriteriaBuilder cb, final String orderBy);

	T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType);

	T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	List<T> findList(final CriteriaBuilder cb);

	List<T> findList(final CriteriaBuilder cb, Integer firstResult,
			Integer maxResults);

	List<T> findList(final CriteriaBuilder cb, final String orderBy);

	List<T> findList(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType);

	List<T> findList(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	Long getListCount(final CriteriaBuilder cb);

	CriteriaBuilder createCriteria();

	CriteriaQuery<T> createQuery();

	CriteriaQuery<T> createQuery(final CriteriaBuilder cb);

	CriteriaQuery<T> createQuery(String orderBy, OrderBy orderByType);

	CriteriaQuery<T> addOrder(CriteriaBuilder cb, String orderBy);

	CriteriaQuery<T> addOrder(CriteriaBuilder cb, String orderBy,
			OrderBy orderByType);
}
