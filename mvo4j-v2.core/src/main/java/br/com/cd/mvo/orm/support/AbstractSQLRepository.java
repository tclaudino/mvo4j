package br.com.cd.mvo.orm.support;

import java.util.List;
import java.util.Map.Entry;

import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.orm.NamedQuery;
import br.com.cd.mvo.orm.SQLRepository;

public abstract class AbstractSQLRepository<T> extends AbstractRepository<T>
		implements SQLRepository<T> {

	public AbstractSQLRepository(Class<T> entityClass,
			RepositoryMetaData metaData) {
		super(entityClass, metaData);
	}

	@Override
	public final T findByQuery(String query,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		boolean isNative = !query.getClass().isAnnotationPresent(
				NamedQuery.class);
		return this.findByQuery(query, isNative, parameters);
	}

	@Override
	public final List<T> findListByQuery(String query,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findListByQuery(query, -1, -1, parameters);
	}

	@Override
	public final List<T> findListByQuery(String query, Integer firstResult,
			Integer maxResults,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		boolean isNative = !query.getClass().isAnnotationPresent(
				NamedQuery.class);
		return this.findListByQuery(query, firstResult, maxResults, isNative,
				parameters);
	}

	protected abstract T findByQuery(
			final String queryString,
			final boolean isNativeQuery,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	protected abstract List<T> findListByQuery(
			final String queryString,
			final Integer firstResult,
			final Integer maxResults,
			final boolean isNativeQuery,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

}
