package br.com.cd.mvo.orm;

import java.util.Collection;
import java.util.Map.Entry;

public interface SQLRepository<T, D> extends Repository<T, D> {

	T findByQuery(final String query, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	Collection<T> findListByQuery(final String query, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	Collection<T> findListByQuery(final String query, final Integer firstResult, final Integer maxResults,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

}