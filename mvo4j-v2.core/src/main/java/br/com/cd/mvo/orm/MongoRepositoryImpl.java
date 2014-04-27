package br.com.cd.mvo.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
	public T findByQuery(String query, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		BasicQuery qry = new BasicQuery(query);

		Criteria c = null;
		for (Entry<String, Object> entry : parameters) {
			if (c == null)
				c = Criteria.where(entry.getKey()).is(entry.getValue());
			else
				c.and(entry.getKey()).is(entry.getValue());
		}
		if (c == null)
			qry.addCriteria(c);

		return operation.findOne(qry, this.entityClass);
	}

	@Override
	public Collection<T> findListByQuery(String query, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findListByQuery(query, -1, -1, parameters);
	}

	@Override
	public Collection<T> findListByQuery(String query, Integer firstResult, Integer maxResults,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return findList(new BasicQuery(query), firstResult, maxResults, parameters);
	}

	@Override
	public T find(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = this.applyLikeMap(map, likeCritiria);

		Criteria criteria = new Criteria();

		like(criteria, newMap);

		return find(criteria);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> findList(Integer firstResult, Integer maxResults, OrderBy orderBy) {

		Query query = new Query();

		if (orderBy != null)
			addOrder(query, orderBy);

		return findList(query, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria, Integer firstResult, Integer maxResults,
			OrderBy orderBy) {

		Query query = new Query();

		if (orderBy != null)
			addOrder(query, orderBy);

		Criteria criteria = like(new Criteria(), this.applyLikeMap(map, likeCritiria));

		query.addCriteria(criteria);

		return findList(query, firstResult, maxResults);
	}

	@Override
	public Long getListCount() {

		Query query = new Query();

		return this.operation.count(query, entityClass);
	}

	@Override
	public Long getListCount(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		Query query = new Query();

		Criteria criteria = like(new Criteria(), this.applyLikeMap(map, likeCritiria));

		query.addCriteria(criteria);

		return this.operation.count(query, entityClass);
	}

	@Override
	public T save(T entity) {

		this.operation.save(entity);
		return entity;
	}

	@Override
	public T update(T entity) {

		this.operation.save(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {

		this.operation.remove(entity);
	}

	@Override
	public T find(Serializable id) {

		return this.operation.findById(id, entityClass);
	}

	private Collection<T> findList(Query query, Integer firstResult, Integer maxResults,
			@SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		Criteria c = null;
		for (Entry<String, Object> entry : parameters) {
			if (c == null)
				c = Criteria.where(entry.getKey()).is(entry.getValue());
			else
				c = new Criteria().andOperator(c, Criteria.where(entry.getKey()).is(entry.getValue()));
		}
		if (c == null)
			query.addCriteria(c);

		boolean all = maxResults == -1;
		int offset = !all && firstResult == -1 ? 0 : firstResult;
		if (!all) {
			query.skip(offset);
			query.limit(maxResults);
		}

		return operation.find(query, this.entityClass);
	}

	private final T find(final Criteria criteria) {

		return operation.findOne(new Query(criteria), this.entityClass);
	}

	private Criteria like(Criteria cb, Map<String, Entry<Object, LikeCritirionEnum>> map) {

		List<Criteria> expressions = new ArrayList<>();
		for (Iterator<Entry<String, Entry<Object, LikeCritirionEnum>>> iterator = map.entrySet().iterator(); iterator.hasNext();) {

			Entry<String, Entry<Object, LikeCritirionEnum>> entry = iterator.next();
			System.out.println("put a like, key: " + entry.getKey() + ", value: " + entry.getValue());

			LikeCritirionEnum likeCritiriaEnum = entry.getValue().getValue();

			Criteria subClause;

			if (!LikeCritirionEnum.NONE.equals(likeCritiriaEnum)) {
				System.out.println("put a like: " + likeCritiriaEnum + " , key: " + entry.getKey() + ", value: "
						+ entry.getValue().getKey());

				if (likeCritiriaEnum.getIgnoreCase())
					subClause = new Criteria().regex(likeCritiriaEnum.getLike(entry.getValue().getKey(), true), "i");
				else
					subClause = new Criteria().regex(likeCritiriaEnum.getLike(entry.getValue().getKey(), true));
			} else {
				System.out.println("put a eq, key: " + entry.getKey() + ", value: " + entry.getValue());
				new Criteria();
				subClause = Criteria.where(entry.getKey()).is(entry.getValue().getValue());
			}
			expressions.add(subClause);
		}

		return cb.andOperator(expressions.toArray(new Criteria[expressions.size()]));
	}

	private void addOrder(Query query, OrderBy orderBy) {

		switch (orderBy.getOrderDirection()) {
		case ASC:
			query.with(new Sort(Sort.Direction.ASC, orderBy.getOrderField()));
		case DESC:
			query.with(new Sort(Sort.Direction.DESC, orderBy.getOrderField()));
		default:
			query.with(new Sort(orderBy.getOrderField()));
		}
	}

}
