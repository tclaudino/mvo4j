package br.com.cd.scaleframework.core.orm.suppport;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import br.com.cd.scaleframework.core.orm.Action;
import br.com.cd.scaleframework.core.orm.ActionWithoutResult;
import br.com.cd.scaleframework.core.orm.LikeCritirion;
import br.com.cd.scaleframework.core.orm.OrderBy;
import br.com.cd.scaleframework.util.ParserUtils;

@SuppressWarnings("rawtypes")
public class AbstractJpaRepository<T, ID extends Serializable> implements
		JpaRepository<T, ID> {

	private EntityManagerFactory managerFactory;;
	private boolean useTransaction = false;

	private ActionListener<T> listener = new ActionListener<T>() {

		@Override
		public void onRead(T entity) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRead(List<T> entity) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean beforeSave(T entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean afterSave(T entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean beforeUpdate(T entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean afterUpdate(T entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean beforeDelete(T entity) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean afterDelete(T entity) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private Class<T> entityClass;

	public AbstractJpaRepository(EntityManagerFactory managerFactory,
			Class<T> entityClass) {
		this.managerFactory = managerFactory;

		// this.entityClass = GenericsUtils.getTypeArguments(
		// AbstractJpaRepository.class, this.getClass()).get(0);
		this.entityClass = entityClass;
	}

	public AbstractJpaRepository(EntityManagerFactory managerFactory,
			Class<T> entityClass, boolean useTransaction) {
		this(managerFactory, entityClass);
		this.useTransaction = useTransaction;
	}

	public void setListener(ActionListener<T> listener) {
		this.listener = listener;
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
							Logger.getLogger(
									AbstractJpaRepository.class.getName()).log(
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
							Logger.getLogger(
									AbstractJpaRepository.class.getName()).log(
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
					listener.onRead(toResult);
				}
				return toResult;
			}
		});
		return setDefault(result);
	}

	protected final T find(final CriteriaQuery<T> criteriaQuery) {

		return doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(EntityManager em) throws SQLException {
				try {
					TypedQuery<T> query = em.createQuery(criteriaQuery);

					T toResult = query.getSingleResult();
					if (toResult != null) {
						listener.onRead(toResult);
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
					if (toResult != null && toResult.size() > 0) {
						listener.onRead(toResult);
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
				// TODO: Check if (entity.isNew()) {

				if (listener.beforeSave(entity)) {
					em.persist(entity);
					listener.afterSave(entity);
				}
				// } else if (!em.contains(entity)) {
				// em.merge(entity);
				// }
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}

	@Override
	public void update(final T entity) {

		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(EntityManager em) {
				// TODO: Check if (entity.isNew()) {
				if (listener.beforeUpdate(entity)) {
					em.merge(entity);
					listener.afterUpdate(entity);
				}
				// } else if (!em.contains(entity)) {
				// em.merge(entity);
				// }
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
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"before DELETE");

				if (listener.beforeDelete(entity)) {
					if (!em.contains(entity)) {
						em.merge(entity);
					}
					em.remove(entity);
					listener.afterDelete(entity);
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
	}

	// </editor-fold>

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
		return this.find(associationPath, propertyName, value,
				LikeCritirion.NONE);
	}

	@Override
	public final T find(String associationPath, String propertyName,
			Object value, LikeCritirion likeType) {
		// select c from ? c where c.?.? = ?
		CriteriaBuilder cb = createCriteria();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());

		cq.select(root).where(
				cb.equal(root.get(associationPath).get(propertyName), value));

		return find(cq);

	}

	// <editor-fold defaultstate="collapsed" desc="find's CriteriaBuilder">
	@Override
	public final T find(final CriteriaBuilder cb) {
		return find(createQuery(cb));
	}

	@Override
	public final T find(final CriteriaBuilder cb, final String orderBy) {
		return find(cb, orderBy, OrderBy.NONE);
	}

	@Override
	public final T find(final CriteriaBuilder cb, final String orderBy,
			final OrderBy orderByType) {
		return find(addOrder(cb, orderBy, orderByType));
	}

	@Override
	public T find(String propertyName, Object value, LikeCritirion likeType) {
		return this.find(new String[] { propertyName }, new Object[] { value },
				likeType);
	}

	@Override
	public T find(Map<String, Object> map) {
		return this.find(map, LikeCritirion.NONE);
	}

	@Override
	public T find(Map<String, Object> map, LikeCritirion likeType) {
		T result = null;
		if (map != null & map.size() > 0) {
			CriteriaQuery<T> criteria = createQuery();
			like(criteria, map, likeType);
			result = find(criteria);
		}
		return setDefault(result);
	}

	@Override
	public T findLikeMap(Map<String, Entry<Object, LikeCritirion>> map) {
		T result = null;
		if (map != null & map.size() > 0) {
			CriteriaQuery<T> criteria = createQuery();

			likeMap(criteria, map);

			result = find(criteria);
		}
		return setDefault(result);
	}

	@Override
	public T find(String[] propertyNames, Object[] values,
			LikeCritirion likeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (propertyNames != null && values != null && propertyNames.length > 0
				&& propertyNames.length == values.length) {
			for (int i = 0; i < propertyNames.length; i++) {
				map.put(propertyNames[i], values[i]);
			}
		}
		return find(map, likeType);
	}

	// </editor-fold>
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
	public final List<T> findList(String associationPath, String propertyName,
			Object value) {
		return this.findList(associationPath, propertyName, value, -1, -1);
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value, final Integer firstResult, final Integer maxResults) {
		return this.findList(associationPath, propertyName, value, firstResult,
				maxResults, LikeCritirion.NONE);
	}

	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, LikeCritirion likeType) {
		return this.findList(associationPath, propertyName, value, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value, final Integer firstResult, final Integer maxResults,
			LikeCritirion likeType) {

		// select c from ? c where c.?.? = ?
		CriteriaBuilder cb = createCriteria();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());

		Map<String, Object> map = map(new String[] { propertyName },
				new Object[] { value });

		like(cq, map, likeType);

		return findList(cq, firstResult, maxResults);
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
	}

	@Override
	public final List<T> findList(String propertyName, Object value) {
		return this.findList(propertyName, value, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value,
			LikeCritirion likeType) {
		return this.findList(propertyName, value, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults) {

		return this.findList(propertyName, value, firstResult, maxResults,
				LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.Integer, java.lang.Integer,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value,
			Integer firstResult, Integer maxResults, LikeCritirion likeType) {

		return this.findList(new String[] { propertyName },
				new Object[] { value }, firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final OrderBy orderByType) {

		return this.findList(propertyName, value, orderBy, orderByType,
				LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.String,
	 * br.com.abril.pce.client.service.OrderBy, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, LikeCritirion likeType) {

		return this.findList(propertyName, value, orderBy, orderByType, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy) {

		return this.findList(propertyName, value, orderBy, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			LikeCritirion likeType) {

		return this.findList(propertyName, value, orderBy, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final Integer firstResult,
			final Integer maxResults) {

		return this.findList(propertyName, value, orderBy, firstResult,
				maxResults, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Integer firstResult, Integer maxResults, LikeCritirion likeType) {

		return this.findList(propertyName, value, orderBy, OrderBy.NONE,
				firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return this.findList(propertyName, value, orderBy, orderByType,
				firstResult, maxResults, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.String,
	 * br.com.abril.pce.client.service.OrderBy, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			LikeCritirion likeType) {

		return this.findList(new String[] { propertyName },
				new Object[] { value }, orderBy, orderByType, firstResult,
				maxResults, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values) {
		return findList(propertyNames, values, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			LikeCritirion likeType) {
		return findList(propertyNames, values, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy) {

		return findList(propertyNames, values, orderBy, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, LikeCritirion likeType) {

		return findList(propertyNames, values, orderBy, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType) {

		return findList(propertyNames, values, orderBy, orderByType,
				LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.String,
	 * br.com.abril.pce.client.service.OrderBy, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, LikeCritirion likeType) {

		return findList(propertyNames, values, orderBy, orderByType, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults) {

		return findList(propertyNames, values, firstResult, maxResults,
				LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.Integer, java.lang.Integer,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Integer firstResult, Integer maxResults, LikeCritirion likeType) {

		System.out.println("findList, to key-value map, propertyNames: "
				+ propertyNames + ", values: " + values + ", firstResult: "
				+ firstResult + ", maxResults: " + maxResults);

		Map<String, Object> map = map(propertyNames, values);

		return findList(map, firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final Integer firstResult,
			final Integer maxResults) {

		return findList(propertyNames, values, orderBy, firstResult,
				maxResults, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.String, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Integer firstResult, Integer maxResults,
			LikeCritirion likeType) {

		return findList(propertyNames, values, orderBy, OrderBy.NONE,
				firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return findList(propertyNames, values, orderBy, orderByType,
				firstResult, maxResults, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.String,
	 * br.com.abril.pce.client.service.OrderBy, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults, LikeCritirion likeType) {

		System.out.println("findList, to key-value map, propertyNames: "
				+ propertyNames + ", values: " + values + ", orderBy: "
				+ orderBy + ", orderByType: " + orderByType + ", firstResult: "
				+ firstResult + ", maxResults: " + maxResults);

		Map<String, Object> map = map(propertyNames, values);

		return findList(map, orderBy, orderByType, firstResult, maxResults,
				likeType);
	}

	@Override
	public final List<T> findList(final Map<String, Object> map) {
		return findList(map, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, LikeCritirion likeType) {
		return findList(map, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(final Map<String, Object> map,
			Integer firstResult, Integer maxResults) {
		return findList(map, firstResult, maxResults, LikeCritirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, LikeCritirion likeType) {

		CriteriaQuery<T> criteria = this.createQuery();

		if (map != null & !map.isEmpty()) {
			like(criteria, map, likeType);
		}
		return findList(criteria, firstResult, maxResults);
	}

	@Override
	public final List<T> findList(Map<String, Object> map, final String orderBy) {
		return findList(map, orderBy, LikeCritirion.NONE);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			LikeCritirion likeType) {
		return findList(map, orderBy, OrderBy.NONE, likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final OrderBy orderByType) {

		return findList(map, orderBy, orderByType, LikeCritirion.NONE);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, LikeCritirion likeType) {

		return findList(map, orderBy, orderByType, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final Integer firstResult,
			final Integer maxResults) {

		return findList(map, orderBy, firstResult, maxResults,
				LikeCritirion.NONE);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults, LikeCritirion likeType) {

		return findList(map, orderBy, OrderBy.NONE, firstResult, maxResults,
				likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return findList(map, orderBy, orderByType, firstResult, maxResults,
				LikeCritirion.NONE);
	}

	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			LikeCritirion likeType) {

		CriteriaBuilder cb = this.createCriteria();
		CriteriaQuery<T> cq = addOrder(cb, orderBy, orderByType);

		if (map != null & !map.isEmpty()) {
			like(cq, map, likeType);
		}
		return findList(cq, firstResult, maxResults);
	}

	// <editor-fold defaultstate="collapsed" desc="findList(Criteria criteria)">

	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, LikeCritirion>> map) {
		return findListLikeMap(map, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<T> findListLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map, Integer firstResult,
			Integer maxResults) {

		CriteriaQuery<T> criteria = createQuery();

		likeMap(criteria, map);

		return findList(criteria, firstResult, maxResults);
	}

	@Override
	public List<T> findListLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map, String orderBy) {

		return findListLikeMap(map, orderBy, OrderBy.NONE);
	}

	@Override
	public List<T> findListLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map, String orderBy,
			OrderBy orderByType) {

		return findListLikeMap(map, orderBy, orderByType, -1, -1);
	}

	@Override
	public List<T> findListLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map, String orderBy,
			Integer firstResult, Integer maxResults) {

		return findListLikeMap(map, orderBy, OrderBy.NONE, -1, -1);
	}

	@Override
	public List<T> findListLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults) {

		CriteriaQuery<T> criteria = createQuery(orderBy, orderByType);

		likeMap(criteria, map);

		return findList(criteria, firstResult, maxResults);
	}

	@Override
	public final T findByQuery(final String queryString,
			final Entry<String, Object>... parameters) {
		return findByQuery(queryString, false, parameters);
	}

	@Override
	public final T findByNativeQuery(final String queryString,
			final Entry<String, Object>... parameters) {
		return findByQuery(queryString, true, parameters);
	}

	private final T findByQuery(final String queryString,
			final boolean isNativeQuery,
			final Entry<String, Object>... parameters) {

		return doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(EntityManager em) throws SQLException {
				try {
					Query query;
					if (isNativeQuery) {
						query = em.createNativeQuery(queryString,
								getEntityClass());
					} else {
						query = em.createQuery(queryString, getEntityClass());
					}

					for (Entry<String, Object> parameter : parameters) {
						if (parameter.getValue().getClass().isArray()) {
							query.setParameter(parameter.getKey(),
									(Object[]) parameter.getValue());
						} else {
							query.setParameter(parameter.getKey(),
									parameter.getValue());
						}
					}

					@SuppressWarnings("unchecked")
					T toResult = (T) query.getSingleResult();
					if (toResult != null) {
						listener.onRead(toResult);
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ em, ex);
				}
			}
		});
	}

	@Override
	public final List<T> findListByQuery(final String queryString,
			final Entry<String, Object>... parameters) {
		return findListByQuery(queryString, -1, -1, parameters);
	}

	@Override
	public final List<T> findListByQuery(final String queryString,
			final Integer firstResult, final Integer maxResults,
			final Entry<String, Object>... parameters) {
		return findListByQuery(queryString, firstResult, maxResults, false,
				parameters);
	}

	@Override
	public final List<T> findListByNativeQuery(final String queryString,
			final Entry<String, Object>... parameters) {
		return findListByNativeQuery(queryString, -1, -1, parameters);
	}

	@Override
	public final List<T> findListByNativeQuery(final String queryString,
			final Integer firstResult, final Integer maxResults,
			final Entry<String, Object>... parameters) {
		return findListByQuery(queryString, firstResult, maxResults, true,
				parameters);
	}

	private final List<T> findListByQuery(final String queryString,
			final Integer firstResult, final Integer maxResults,
			final boolean isNativeQuery,
			final Entry<String, Object>... parameters) {

		return doNonTransaction(provider, new Action<List<T>>() {

			@Override
			public List<T> execute(EntityManager em) throws SQLException {
				try {
					Query query;
					if (isNativeQuery) {
						query = em.createNativeQuery(queryString,
								getEntityClass());
					} else {
						query = em.createQuery(queryString, getEntityClass());
					}

					for (Entry<String, Object> parameter : parameters) {
						if (parameter.getValue().getClass().isArray()) {
							query.setParameter(parameter.getKey(),
									(Object[]) parameter.getValue());
						} else {
							query.setParameter(parameter.getKey(),
									parameter.getValue());
						}
					}

					boolean all = maxResults == -1;
					int offset = !all && firstResult == -1 ? 0 : firstResult;
					if (!all) {
						query.setMaxResults(maxResults);
						query.setFirstResult(offset);
					}
					@SuppressWarnings("unchecked")
					List<T> toResult = (List<T>) query.getResultList();
					if (toResult != null && toResult.size() > 0) {
						listener.onRead(toResult);
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ em, ex);
				}
			}
		});
	}

	// </editor-fold>
	// </editor-fold>

	@Override
	public final Long getListCount() {
		return getListCount(createCriteria());
	}

	@Override
	public Long getListCount(String propertyName, Object value,
			LikeCritirion likeType) {
		return this.getListCount(new String[] { propertyName },
				new Object[] { value }, likeType);
	}

	@Override
	public final Long getListCount(String[] propertyNames, Object[] values) {
		return this.getListCount(propertyNames, values, LikeCritirion.NONE);
	}

	@Override
	public final Long getListCount(String propertyName, Object value) {
		return this.getListCount(propertyName, value, LikeCritirion.NONE);
	}

	@Override
	public Long getListCount(String[] propertyNames, Object[] values,
			LikeCritirion likeType) {

		Map<String, Object> map = map(propertyNames, values);

		return getListCount(map, likeType);
	}

	@Override
	public final Long getListCount(Map<String, Object> map) {

		return getListCount(map, LikeCritirion.NONE);
	}

	@Override
	public Long getListCount(Map<String, Object> map, LikeCritirion likeType) {

		CriteriaBuilder cb = this.createCriteria();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		if (map != null & !map.isEmpty()) {
			like(cq, map, likeType);
		}
		return getListCount(cb, cq);
	}

	@Override
	public Long getListCountLikeMap(
			Map<String, Entry<Object, LikeCritirion>> map) {

		CriteriaBuilder cb = this.createCriteria();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		likeMap(cq, map);

		return getListCount(cb, cq);
	}

	@Override
	public final Long getListCount(final CriteriaBuilder cb) {

		CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		return this.getListCount(cb, cq);
	}

	private final Long getListCount(final CriteriaBuilder cb,
			final CriteriaQuery<Long> cq) {

		return doInTransaction(provider, new Action<Long>() {

			@Override
			public Long execute(EntityManager em) throws SQLException {
				try {

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

	private Map<String, Object> map(String[] propertyNames, Object[] values) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (propertyNames != null && values != null && propertyNames.length > 0
				&& propertyNames.length == values.length) {
			for (int i = 0; i < propertyNames.length; i++) {
				System.out.println("maping key-value key: " + propertyNames[i]
						+ ", value: " + values[i]);
				map.put(propertyNames[i], values[i]);
			}
		}
		return map;
	}

	private void like(CriteriaQuery criteria, Map<String, Object> map,
			LikeCritirion likeType) {

		CriteriaBuilder criteriaBuilder = this.createCriteria();
		Root<T> root = criteria.from(this.getEntityClass());

		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			System.out.println("put a like, key: " + entry.getKey()
					+ ", value: " + entry.getValue());

			Expression expression;
			if (!LikeCritirion.NONE.equals(entry.getValue())) {
				System.out.println("put a like, key: " + entry.getKey()
						+ ", value: " + entry.getValue());

				expression = criteriaBuilder.like(
						root.<String> get(entry.getKey()),
						likeType.getLike(entry.getKey()));
				if (likeType.getIgnoreCase()) {
					expression = criteriaBuilder.lower(expression);
				}
			} else {
				System.out.println("put a eq, key: " + entry.getKey()
						+ ", value: " + entry.getValue());
				expression = criteriaBuilder.equal(root.get(entry.getKey()),
						likeType.getLike(entry.getKey()));
			}
			criteria.where(expression);
		}
	}

	private void likeMap(CriteriaQuery criteria,
			Map<String, Entry<Object, LikeCritirion>> map) {

		CriteriaBuilder criteriaBuilder = this.createCriteria();
		Root<T> root = criteria.from(this.getEntityClass());

		Iterator<Entry<String, Entry<Object, LikeCritirion>>> iterator = map
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Entry<Object, LikeCritirion>> entryMap = iterator
					.next();

			Entry<Object, LikeCritirion> entry = entryMap.getValue();

			Expression expression;
			LikeCritirion likeType = entry.getValue();
			if (!LikeCritirion.NONE.equals(entry.getValue())) {
				System.out.println("put a like, key: " + entry.getKey()
						+ ", value: " + entry.getValue());

				expression = criteriaBuilder.like(
						root.<String> get(entryMap.getKey()),
						likeType.getLike(entry.getKey()));
				if (likeType.getIgnoreCase()) {
					expression = criteriaBuilder.lower(expression);
				}
			} else {
				System.out.println("put a eq, key: " + entry.getKey()
						+ ", value: " + entry.getValue());
				expression = criteriaBuilder.equal(root.get(entryMap.getKey()),
						likeType.getLike(entry.getKey()));
			}
			criteria.where(expression);
		}
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