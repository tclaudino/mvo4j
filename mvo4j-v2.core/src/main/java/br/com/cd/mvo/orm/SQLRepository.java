package br.com.cd.mvo.orm;

import java.util.List;
import java.util.Map.Entry;

public interface SQLRepository<T> extends ListenableRepository<T> {

	T findByQuery(
			final String query,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	List<T> findListByQuery(
			final String query,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

	List<T> findListByQuery(
			final String query,
			final Integer firstResult,
			final Integer maxResults,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters);

}