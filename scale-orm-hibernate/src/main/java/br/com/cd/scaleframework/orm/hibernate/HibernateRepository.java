package br.com.cd.scaleframework.orm.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import br.com.cd.scaleframework.orm.OrderBy;
import br.com.cd.scaleframework.orm.Repository;

public interface HibernateRepository<T, ID extends Serializable> extends
		Repository<T, ID> {

	T find(final DetachedCriteria dc);

	List<T> findList(final DetachedCriteria dc);

	List<T> findList(final DetachedCriteria dc, Integer firstResult,
			Integer maxResults);

	List<T> findList(final DetachedCriteria dc, final String orderBy);

	List<T> findList(final DetachedCriteria dc, final String orderBy,
			final OrderBy orderByType);

	List<T> findList(final DetachedCriteria dc, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults);

	Long getListCount(final DetachedCriteria dc);
}
