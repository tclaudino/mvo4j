package br.com.cd.mvo.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.mongodb.core.MongoOperations;

import br.com.cd.mvo.core.RepositoryMetaData;

public class MongoRepositoryImpl<T> extends AbstractSQLRepository<T, MongoOperations> implements MongoRepository<T> {

	private final MongoOperations operation;

	public MongoRepositoryImpl(MongoOperations operation, RepositoryMetaData<T> metaData) {
		super(metaData);
		this.operation = operation;
	}

	@Override
	public MongoOperations getDelegate() {

		return operation;
	}

	@Override
	public T findByQuery(String query, Entry<String, Object>... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> findListByQuery(String query, Entry<String, Object>... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> findListByQuery(String query, Integer firstResult, Integer maxResults, Entry<String, Object>... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T find(Map<String, Object> map, LikeCritirionEnum likeCritiria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> findList(Integer firstResult, Integer maxResults, OrderBy orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria, Integer firstResult, Integer maxResults,
			OrderBy orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getListCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getListCount(Map<String, Object> map, LikeCritirionEnum likeCritiria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T save(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T update(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public T find(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

}
