package br.com.cd.scaleframework.jpa;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.cd.model.Entity;
import org.cd.util.ParserUtils;
import org.cd.util.ReflectUtils;

import br.com.cd.scaleframework.jpa.OrderBy;

@SuppressWarnings("rawtypes")
public abstract class AbstractJpaRepository<T extends Entity, ID extends Serializable>
		implements JpaRepository<T, ID> {

	protected abstract void beforeClose(T entity);

	private EntityManagerFactory managerFactory;;
	private boolean useTransaction = false;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public AbstractJpaRepository(EntityManagerFactory managerFactory) {
		this.managerFactory = managerFactory;

		this.entityClass = ReflectUtils.getTypeArguments(AbstractJpaRepository.class,
				this.getClass()).get(0);
	}

	public AbstractJpaRepository(EntityManagerFactory managerFactory,
			boolean useTransaction) {
		this(managerFactory);
		this.useTransaction = useTransaction;
	}

	protected Class<T> getEntityClass() {
		return entityClass;
	}

	protected final EntityManager createEntityManager() {
		return managerFactory.createEntityManager();
	}

	interface Provider {

		<TResult> TResult doInTransaction(Action<TResult> action)
				throws SQLException, Exception;

		void doInTransaction(ActionWithoutResult action) throws SQLException,
				Exception;

		<TResult> TResult doNonTransaction(Action<TResult> action)
				throws SQLException, Exception;

		void doNonTransaction(ActionWithoutResult action) throws SQLException,
				Exception;
	}

	// <editor-fold defaultstate="collapsed" desc="doWithoutTransaction">
	private Provider provider = new Provider() {

		@Override
		public final <TResult> TResult doNonTransaction(Action<TResult> action)
				throws SQLException, Exception {
			EntityManager em = createEntityManager();
			if (em != null) {
				try {
					TResult result = action.execute(em);
					return result;
				} catch (Exception e) {
					// throw new DaoException(getEntityClass(), e);
					throw e;
				} finally {
					em.close();
				}
			} else {
				// throw new DaoException(getEntityClass(),
				// new PersistenceException("coudn't create EntityManager"));
				throw new SQLException(new PersistenceException(
						"coudn't create EntityManager"));
			}
		}

		@Override
		public final void doNonTransaction(ActionWithoutResult action)
				throws SQLException, Exception {
			EntityManager em = createEntityManager();
			if (em != null) {
				try {
					action.execute(em);
				} catch (Exception e) {
					// throw new DaoException(getEntityClass(), e);
					throw e;
				} finally {
					em.close();
				}
			} else {
				// throw new DaoException(getEntityClass(),
				// new PersistenceException("coudn't create EntityManager"));
				throw new SQLException(new PersistenceException(
						"coudn't create EntityManager"));
			}
		}// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="doInTransaction">
		@SuppressWarnings("unused")
		@Override
		public final <TResult> TResult doInTransaction(Action<TResult> action)
				throws SQLException, Exception {
			EntityManager em = createEntityManager();
			if (em != null) {
				EntityTransaction tx = em.getTransaction();
				if (em != null) {
					try {
						// @todo tx.begin();
						TResult result = action.execute(em);
						tx.commit();
						return result;
					} catch (Exception e) {
						try {
							em.getTransaction().rollback();
						} catch (Exception ex) {
							Logger.getLogger(AbstractJpaRepository.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						// throw new DaoException(getEntityClass(), e);
						throw e;
					} finally {
						em.close();
					}
				} else {
					// throw new DaoException(getEntityClass(),
					// new
					// PersistenceException("coudn't initalize transaction"));
					throw new SQLException(new PersistenceException(
							"coudn't create EntityManager"));
				}
			} else {
				// throw new DaoException(
				// getEntityClass(),new
				// PersistenceException("coudn't create EntityManager"));
				throw new SQLException(new PersistenceException(
						"coudn't create EntityManager"));
			}
		}

		@Override
		public final void doInTransaction(ActionWithoutResult action)
				throws SQLException, Exception {
			EntityManager em = createEntityManager();
			if (em != null) {
				EntityTransaction tx = em.getTransaction();
				if (tx != null) {
					try {
						// @todo tx.begin();
						action.execute(em);
						tx.commit();
					} catch (Exception e) {
						try {
							em.getTransaction().rollback();
						} catch (Exception ex) {
							Logger.getLogger(AbstractJpaRepository.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						// throw new DaoException(getEntityClass(), e);
						throw e;
					} finally {
						em.close();
					}
				} else {
					// throw new DaoException(getEntityClass(),
					// new
					// PersistenceException("coudn't initalize transaction"));
					throw new SQLException(new PersistenceException(
							"coudn't create EntityManager"));
				}
			} else {
				// throw new DaoException(getEntityClass(),
				// new PersistenceException("coudn't create EntityManager"));
				throw new SQLException(new PersistenceException(
						"coudn't create EntityManager"));
			}
		}
	};// </editor-fold>

	private <TResult> TResult doInTransaction(Provider provider,
			Action<TResult> action) throws PersistenceException {
		try {
			return provider.doInTransaction(action);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void doInTransaction(Provider provider, ActionWithoutResult action)
			throws PersistenceException {
		try {
			provider.doInTransaction(action);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private <TResult> TResult doNonTransaction(Provider provider,
			Action<TResult> action) throws PersistenceException {
		try {
			return provider.doNonTransaction(action);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void doNonTransaction(Provider provider, ActionWithoutResult action)
			throws PersistenceException {
		try {
			provider.doNonTransaction(action);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="nonTransactionals">
	@Override
	public final T find(final ID id) {
		T result = doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(EntityManager em) {
				T toResult = em.find(getEntityClass(), id);
				if (toResult != null) {
					beforeClose(toResult);
				}
				return toResult;
			}
		});
		return setDefault(result);
	}

	protected final T find(final CriteriaQuery<T> criteriaQuery,
			final int firstResult, final int maxResults) {

		return doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(EntityManager em) throws SQLException {
				try {
					TypedQuery<T> query = em.createQuery(criteriaQuery);
					boolean all = maxResults == -1;
					int offset = !all && firstResult == -1 ? 0 : firstResult;
					if (!all) {
						query.setMaxResults(maxResults);
						query.setFirstResult(offset);
					}
					T toResult = query.getSingleResult();
					if (toResult != null) {
						beforeClose(toResult);
					}
					return toResult;
				} catch (Exception ex) {
					// throw new DaoException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in EntityManager: "
					// + em);
					throw new SQLException(
							"coudn't initialize executableCriteria in EntityManager: "
									+ em, ex);
				}
			}
		});
	}

	protected final List<T> findList(final CriteriaQuery<T> criteriaQuery,
			final int firstResult, final int maxResults) {

		return doNonTransaction(provider, new Action<List<T>>() {

			@Override
			public List<T> execute(EntityManager em) throws SQLException {
				try {
					TypedQuery<T> query = em.createQuery(criteriaQuery);
					boolean all = maxResults == -1;
					int offset = !all && firstResult == -1 ? 0 : firstResult;
					if (!all) {
						query.setMaxResults(maxResults);
						query.setFirstResult(offset);
					}
					List<T> toResult = query.getResultList();
					if (toResult != null) {
						for (T entity : toResult) {
							beforeClose(entity);
						}
					}
					return toResult;
				} catch (Exception ex) {
					// throw new DaoException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in EntityManager: "
					// + em);
					throw new SQLException(
							"coudn't initialize executableCriteria in EntityManager: "
									+ em, ex);
				}
			}
		});
	}// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Transactionals">
	@Override
	public final void save(final T entity) {

		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(EntityManager em) {
				if (entity.isNew()) {
					em.persist(entity);
				} else if (!em.contains(entity)) {
					em.merge(entity);
				}
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}

	@Override
	public final void delete(final T entity) {

		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(EntityManager em) {
				if (!em.contains(entity)) {
					em.merge(entity);
				}
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"before DELETE");
				em.remove(entity);
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"after DELETE");
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}

	@Override
	public final void delete(final List<T> entityList) {

		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(EntityManager em) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"before DELETE");
				for (T entity : entityList) {
					if (!em.contains(entity)) {
						em.merge(entity);
					}
					em.remove(entity);
				}
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"after DELETE");
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="find's">
	@Override
	public final T find(String propertyName, Object value) {
		return this.find(new String[] { propertyName }, new Object[] { value });
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T find(String[] propertyNames, Object[] values) {
		Map map = new HashMap();
		if (propertyNames != null && values != null && propertyNames.length > 0
				&& propertyNames.length == values.length) {
			for (int i = 0; i < propertyNames.length; i++) {
				map.put(propertyNames[i], values[i]);
			}
		}
		return find(map);
	}

	@Override
	public final T find(String associationPath, String propertyName,
			Object value) {
		return this.find(associationPath, propertyName, value, -1, -1);
	}

	@Override
	public final T find(String associationPath, String propertyName,
			Object value, final Integer firstResult, final Integer maxResults) {
		// select c from ? c where c.?.? = ?
		CriteriaBuilder cb = createCriteria();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());

		cq.select(root).where(
				cb.equal(root.get(associationPath).get(propertyName), value));

		return find(cq, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T find(final Map map) {
		T result = null;
		if (map != null & !map.isEmpty()) {
			CriteriaBuilder cb = createCriteria();
			CriteriaQuery cq = createQuery(cb);
			Root<T> root = cq.from(getEntityClass());

			Iterator iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String key = ParserUtils.parseString(iterator.next());
				Object value = map.get(key);

				if (!"".equals(value) && value != null) {
					cb.equal(root.get(key), value);
				}
			}
			result = find(cb);
		}
		return setDefault(result);
	}

	// <editor-fold defaultstate="collapsed" desc="find's CriteriaBuilder">
	@Override
	public final T find(final CriteriaBuilder cb) {
		return find(cb, -1, -1);
	}

	@Override
	public final T find(final CriteriaBuilder cb, Integer firstResult,
			Integer maxResults) {
		return find(createQuery(cb), firstResult, maxResults);
	}

	@Override
	public final T find(final CriteriaBuilder cb, final String orderBy) {
		return find(cb, orderBy, OrderBy.NONE, -1, -1);
	}

	@Override
	public final T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType) {
		return find(cb, orderBy, orderByType, -1, -1);
	}

	@Override
	public final T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults) {
		return find(addOrder(cb, orderBy, orderByType), firstResult, maxResults);
	}// </editor-fold>
		// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="findList's">
	@Override
	public final List<T> findList() {
		return this.findList(-1, -1);
	}

	@Override
	public final List<T> findList(final Integer firstResult,
			final Integer maxResults) {
		return findList(createQuery(), firstResult, maxResults);
	}

	@Override
	public final List<T> findList(String orderBy) {
		return this.findList(orderBy, OrderBy.NONE);
	}

	@Override
	public final List<T> findList(String orderBy, OrderBy orderByType) {
		return this.findList(orderBy, orderByType, -1, -1);
	}

	@Override
	public final List<T> findList(String orderBy, Integer firstResult,
			Integer maxResults) {
		return this.findList(orderBy, OrderBy.NONE, firstResult, maxResults);
	}

	@Override
	public final List<T> findList(final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults) {
		return findList(createQuery(orderBy, orderByType), firstResult,
				maxResults);
	}

	@Override
	public final List<T> findList(String propertyName, Object value) {
		return this.findList(new String[] { propertyName },
				new Object[] { value });
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value) {
		return this.findList(associationPath, propertyName, value, -1, -1);
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value, final Integer firstResult, final Integer maxResults) {
		// select c from ? c where c.?.? = ?
		CriteriaBuilder cb = createCriteria();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());

		cq.select(root).where(
				cb.equal(root.get(associationPath).get(propertyName), value));

		return findList(cq, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final List<T> findList(String[] propertyNames, Object[] values) {
		Map map = new HashMap();
		if (propertyNames != null && values != null && propertyNames.length > 0
				&& propertyNames.length == values.length) {
			for (int i = 0; i < propertyNames.length; i++) {
				map.put(propertyNames[i], values[i]);
			}
		}
		return findList(map);
	}

	@Override
	public final List<T> findList(final Map<String, Object> map) {
		return findList(map, -1, -1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final List<T> findList(final Map<String, Object> map,
			Integer firstResult, Integer maxResults) {
		List<T> result = new ArrayList<T>();
		CriteriaBuilder cb = createCriteria();

		if (map != null) {
			CriteriaQuery cq = createQuery(cb);
			Root<T> root = cq.from(getEntityClass());

			for (Map.Entry<String, Object> entry : map.entrySet()) {

				if (!"".equals(entry.getValue()) && entry.getValue() != null) {
					cb.equal(root.get(entry.getKey()), entry.getValue());
				}
			}
			result = findList(cb, firstResult, maxResults);
		}
		return result;
	}

	// <editor-fold defaultstate="collapsed" desc="findList's CriteriaBuilder">
	@Override
	public final List<T> findList(final CriteriaBuilder cb) {
		return findList(cb, -1, -1);
	}

	@Override
	public final List<T> findList(final CriteriaBuilder cb,
			Integer firstResult, Integer maxResults) {
		return findList(createQuery(cb), firstResult, maxResults);
	}

	@Override
	public final List<T> findList(final CriteriaBuilder cb, final String orderBy) {
		return findList(cb, orderBy, OrderBy.NONE, -1, -1);
	}

	@Override
	public final List<T> findList(final CriteriaBuilder cb,
			final String orderBy, final OrderBy orderByType) {
		return findList(cb, orderBy, orderByType, -1, -1);
	}

	@Override
	public final List<T> findList(final CriteriaBuilder cb,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {
		return findList(addOrder(cb, orderBy, orderByType), firstResult,
				maxResults);
	}// </editor-fold>
		// </editor-fold>

	@Override
	public final Long getListCount() {
		return getListCount(createCriteria());
	}

	@Override
	public final Long getListCount(final CriteriaBuilder cb) {

		return doInTransaction(provider, new Action<Long>() {

			@Override
			public Long execute(EntityManager em) throws SQLException {
				try {
					CriteriaQuery<Long> cq = cb.createQuery(Long.class);

					Root<T> root = cq.from(getEntityClass());

					cq.select(cb.count(root));

					TypedQuery<Long> query = em.createQuery(cq);

					return ParserUtils.parseLong(query.getSingleResult());

				} catch (Exception ex) {
					// throw new DaoException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in EntityManager: "
					// + em);
					throw new SQLException(
							"coudn't initialize executableCriteria in EntityManager: "
									+ em, ex);
				}
			}
		});
	}

	// <editor-fold defaultstate="collapsed"
	// desc="createCriteria && createQuery">
	@Override
	public final CriteriaBuilder createCriteria() {
		return managerFactory.getCriteriaBuilder();
	}

	@Override
	public final CriteriaQuery<T> createQuery() {
		return createQuery(createCriteria());
	}

	@Override
	public final CriteriaQuery<T> createQuery(final CriteriaBuilder cb) {
		return cb.createQuery(getEntityClass());
	}

	@Override
	public final CriteriaQuery<T> createQuery(String orderBy,
			OrderBy orderByType) {
		CriteriaBuilder cb = createCriteria();
		CriteriaQuery<T> cq = createQuery(cb);
		Root<T> root = cq.from(getEntityClass());
		if (orderByType == OrderBy.ASC) {
			cq.orderBy(cb.asc(root.get(orderBy)));
		} else if (orderByType == OrderBy.DESC) {
			cq.orderBy(cb.desc(root.get(orderBy)));
		}
		return cq;
	}// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="addOrder">
	@Override
	public final CriteriaQuery<T> addOrder(CriteriaBuilder cb, String orderBy) {
		return addOrder(cb, orderBy, OrderBy.DESC);
	}

	@Override
	public final CriteriaQuery<T> addOrder(CriteriaBuilder cb, String orderBy,
			OrderBy orderByType) {
		CriteriaQuery<T> cq = createQuery(cb);
		Root<T> root = cq.from(getEntityClass());
		if (orderByType == OrderBy.ASC) {
			cq.orderBy(cb.asc(root.get(orderBy)));
		} else if (orderByType == OrderBy.DESC) {
			cq.orderBy(cb.desc(root.get(orderBy)));
		}
		return cq;
	}// </editor-fold>

	private T setDefault(T entity) {
		if (entity == null) {
			try {
				entity = this.getEntityClass().newInstance();
			} catch (InstantiationException ex) {
				Logger.getLogger(getClass().getName()).log(
						Level.SEVERE,
						"Can't found default contructor for class:  "
								+ this.getEntityClass(), ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(getClass().getName()).log(
						Level.SEVERE,
						"Can't found default contructor for class:  "
								+ this.getEntityClass(), ex);
			}
		}
		return entity;
	}
}
