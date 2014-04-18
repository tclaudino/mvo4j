package br.com.cd.mvo.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.util.ParserUtils;

public class JpaRepositoryImpl<T> extends AbstractSQLRepository<T, EntityManager> implements JpaRepository<T> {

	private final EntityManager em;

	public JpaRepositoryImpl(EntityManager em, RepositoryMetaData<T> metaData) {
		super(metaData);
		this.em = em;
	}

	@Override
	public final EntityManager getDelegate() {
		return em;
	}

	@Override
	public T save(final T entity) {

		em.persist(entity);
		return entity;
	}

	@Override
	public T update(T entity) {

		return em.merge(entity);
	}

	@Override
	public void delete(final T entity) {

		em.remove(entity);
	}

	@Override
	public T find(final Serializable id) {

		return em.find(entityClass, id);
	}

	@Override
	public T find(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = this.applyLikeMap(map, likeCritiria);

		CriteriaQuery<T> criteria = createCriteriaQuery();
		like(em.getCriteriaBuilder(), criteria, newMap);

		return find(criteria);
	}

	@Override
	public Collection<T> findList(Integer firstResult, Integer maxResults, OrderBy orderBy) {

		CriteriaQuery<T> cq = orderBy != null ? createCriteriaQuery(orderBy) : createCriteriaQuery();
		return findList(cq, firstResult, maxResults);
	}

	@Override
	public Collection<T> findList(Map<String, Object> map, LikeCritirionEnum likeCritiria, Integer firstResult, Integer maxResults,
			OrderBy orderBy) {

		CriteriaQuery<T> cq = createCriteriaQuery();
		if (orderBy != null)
			addOrder(em.getCriteriaBuilder(), cq, orderBy);

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = this.applyLikeMap(map, likeCritiria);

		like(em.getCriteriaBuilder(), cq, newMap);

		return this.findList(cq, firstResult, maxResults);
	}

	@Override
	public Long getListCount() {

		return this.getListCount(em.getCriteriaBuilder());
	}

	@Override
	public Long getListCount(Map<String, Object> map, LikeCritirionEnum likeCritiria) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		Map<String, Entry<Object, LikeCritirionEnum>> newMap = this.applyLikeMap(map, likeCritiria);

		like(cb, cq, newMap);

		return this.getListCount(cb, cq);
	}

	@Override
	public T find(CriteriaBuilder cb) {

		return this.find(cb, null);
	}

	@Override
	public T find(CriteriaBuilder cb, OrderBy orderBy) {

		CriteriaQuery<T> cq = createCriteriaQuery();
		if (orderBy != null)
			addOrder(em.getCriteriaBuilder(), cq, orderBy);

		return this.find(cq);
	}

	@Override
	public Collection<T> findList(CriteriaBuilder cb) {

		return this.findList(cb, null);
	}

	@Override
	public Collection<T> findList(CriteriaBuilder cb, Integer firstResult, Integer maxResults) {

		return this.findList(cb, null, firstResult, maxResults);
	}

	@Override
	public Collection<T> findList(CriteriaBuilder cb, OrderBy orderBy) {

		return this.findList(cb, orderBy, -1, -1);
	}

	@Override
	public Collection<T> findList(CriteriaBuilder cb, OrderBy orderBy, Integer firstResult, Integer maxResults) {

		CriteriaQuery<T> cq = createCriteriaQuery();
		if (orderBy != null)
			addOrder(em.getCriteriaBuilder(), cq, orderBy);

		return this.findList(cq, firstResult, maxResults);
	}

	@Override
	public Long getListCount(CriteriaBuilder cb) {

		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		return this.getListCount(cb, cq);
	}

	@Override
	public CriteriaQuery<T> createCriteriaQuery() {
		return createCriteriaQuery(em.getCriteriaBuilder());
	}

	@Override
	public CriteriaQuery<T> createCriteriaQuery(CriteriaBuilder cb) {
		return cb.createQuery(this.entityClass);
	}

	@Override
	public CriteriaQuery<T> createCriteriaQuery(OrderBy orderBy) {
		return createCriteriaQuery(em.getCriteriaBuilder(), orderBy);
	}

	@Override
	public CriteriaQuery<T> createCriteriaQuery(CriteriaBuilder cb, OrderBy orderBy) {

		CriteriaQuery<T> cq = this.createCriteriaQuery(cb);
		addOrder(cb, cq, orderBy);

		return cq;
	}

	protected final T find(final CriteriaQuery<T> criteriaQuery) {

		Root<T> root = criteriaQuery.from(this.entityClass);
		criteriaQuery.select(root);

		TypedQuery<T> query = em.createQuery(criteriaQuery);

		T toResult = null;
		List<T> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			toResult = resultList.get(0);
		}

		return toResult;
	}

	protected final Collection<T> findList(final CriteriaQuery<T> criteriaQuery, final int firstResult, final int maxResults) {

		Root<T> root = criteriaQuery.from(this.entityClass);
		criteriaQuery.select(root);

		TypedQuery<T> query = em.createQuery(criteriaQuery);

		boolean all = maxResults == -1;
		int offset = !all && firstResult == -1 ? 0 : firstResult;
		if (!all) {
			query.setMaxResults(maxResults);
			query.setFirstResult(offset);
		}
		Collection<T> toResult = query.getResultList();
		return toResult;
	}

	@Override
	public final T findByQuery(String query, @SuppressWarnings("unchecked") Entry<String, Object>... parameters) {

		return this.findByQuery0(query, false, parameters);
	}

	@Override
	public final T findByNamedQuery(final String queryString, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		return this.findByQuery0(queryString, true, parameters);
	}

	private T findByQuery0(final String queryString, final boolean isNamedQuery,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		Query query;
		if (isNamedQuery) {
			query = em.createQuery(queryString, this.entityClass);
		} else {
			query = em.createNativeQuery(queryString, this.entityClass);
		}

		for (Entry<String, Object> parameter : parameters) {
			if (parameter.getValue().getClass().isArray()) {
				query.setParameter(parameter.getKey(), (Object[]) parameter.getValue());
			} else {
				query.setParameter(parameter.getKey(), parameter.getValue());
			}
		}

		@SuppressWarnings("unchecked")
		List<T> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			T toResult = resultList.get(0);
			return toResult;
		}
		return null;
	}

	@Override
	public final Collection<T> findListByQuery(final String queryString, final Integer firstResult, final Integer maxResults,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		return this.findListByQuery0(queryString, firstResult, maxResults, false, parameters);
	}

	@Override
	public final Collection<T> findListByNamedQuery(final String queryString, final Integer firstResult, final Integer maxResults,
			@SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		return this.findListByQuery0(queryString, firstResult, maxResults, true, parameters);
	}

	@Override
	public Collection<T> findListByQuery(String queryString, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		return this.findListByQuery0(queryString, -1, -1, false, parameters);
	}

	@Override
	public Collection<T> findListByNamedQuery(String queryString, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		return this.findListByQuery0(queryString, -1, -1, true, parameters);
	}

	private final Collection<T> findListByQuery0(final String queryString, final Integer firstResult, final Integer maxResults,
			final boolean isNamedQuery, @SuppressWarnings("unchecked") final Entry<String, Object>... parameters) {

		Query query;
		if (isNamedQuery) {
			query = em.createQuery(queryString, this.entityClass);
		} else {
			query = em.createNativeQuery(queryString, this.entityClass);
		}

		for (Entry<String, Object> parameter : parameters) {
			if (parameter.getValue().getClass().isArray()) {
				query.setParameter(parameter.getKey(), (Object[]) parameter.getValue());
			} else {
				query.setParameter(parameter.getKey(), parameter.getValue());
			}
		}

		boolean all = maxResults == -1;
		int offset = !all && firstResult == -1 ? 0 : firstResult;
		if (!all) {
			query.setMaxResults(maxResults);
			query.setFirstResult(offset);
		}
		@SuppressWarnings("unchecked")
		Collection<T> toResult = (Collection<T>) query.getResultList();
		return toResult;
	}

	protected final Long getListCount(final CriteriaBuilder cb, final CriteriaQuery<Long> cq) {

		Root<T> root = cq.from(this.entityClass);
		cq.select(cb.count(root));

		TypedQuery<Long> query = em.createQuery(cq);

		return ParserUtils.parseLong(query.getSingleResult());
	}

	protected void addOrder(CriteriaBuilder cb, CriteriaQuery<T> cq, OrderBy orderBy) {
		Root<T> root = cq.from(this.entityClass);

		switch (orderBy.getOrderDirection()) {
		case ASC:
			cq.orderBy(cb.asc(root.get(orderBy.getOrderField())));
		case DESC:
			cq.orderBy(cb.desc(root.get(orderBy.getOrderField())));
		default:
			cq.orderBy(cb.desc(root.get(orderBy.getOrderField())));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void like(CriteriaBuilder cb, CriteriaQuery<?> cq, Map<String, Entry<Object, LikeCritirionEnum>> map) {

		Root<T> root = cq.from(this.entityClass);

		Expression expression = null;
		for (Iterator<Entry<String, Entry<Object, LikeCritirionEnum>>> iterator = map.entrySet().iterator(); iterator.hasNext();) {

			Entry<String, Entry<Object, LikeCritirionEnum>> entry = iterator.next();
			System.out.println("put a like, key: " + entry.getKey() + ", value: " + entry.getValue());

			LikeCritirionEnum likeCritiriaEnum = entry.getValue().getValue();

			Expression subClause;
			if (!LikeCritirionEnum.NONE.equals(likeCritiriaEnum)) {
				System.out.println("put a like: " + likeCritiriaEnum + " , key: " + entry.getKey() + ", value: "
						+ entry.getValue().getKey());

				Path<String> path = root.<String> get(entry.getKey());
				subClause = cb.like(likeCritiriaEnum.getIgnoreCase() ? path : cb.lower(path),
						likeCritiriaEnum.getLike(entry.getValue().getKey()));
			} else {
				System.out.println("put a eq, key: " + entry.getKey() + ", value: " + entry.getValue());
				subClause = cb.equal(root.get(entry.getKey()), likeCritiriaEnum.getLike(entry.getValue().getKey()));
			}
			if (expression != null)
				expression = cb.and(expression, subClause);
			else
				expression = subClause;
		}
		if (expression != null)
			cq.where(expression);
	}
}
