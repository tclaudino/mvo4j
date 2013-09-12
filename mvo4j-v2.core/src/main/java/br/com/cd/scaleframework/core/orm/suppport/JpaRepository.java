package br.com.cd.scaleframework.core.orm.suppport;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import br.com.cd.scaleframework.core.orm.OrderBy;
import br.com.cd.scaleframework.core.orm.Service;

public interface JpaRepository<T, ID extends Serializable> extends
		Service<T, ID> {

	T find(final CriteriaBuilder cb);

	T find(final CriteriaBuilder cb, final String orderBy);

	T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType);

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
