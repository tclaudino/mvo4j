package br.com.cd.scaleframework.orm.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import br.com.cd.scaleframework.orm.Critirion;
import br.com.cd.scaleframework.orm.OrderBy;
import br.com.cd.scaleframework.util.GenericsUtils;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;

public abstract class AbstractHibernateRepository<T, ID extends Serializable>
		implements HibernateRepository<T, ID> {

	protected abstract void beforeClose(T entity);

	private SessionFactory sessionFactory;
	private boolean useTransaction = false;

	public AbstractHibernateRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public AbstractHibernateRepository(SessionFactory sessionFactory,
			boolean useTransaction) {
		this(sessionFactory);
		this.useTransaction = useTransaction;
	}

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			this.entityClass = GenericsUtils.getTypeArguments(
					AbstractHibernateRepository.class, this.getClass()).get(0);
		}
		return entityClass;
	}

	protected final Session openSession() throws SQLException {
		return sessionFactory.openSession();
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

	private Provider provider = new Provider() {

		// <editor-fold defaultstate="collapsed" desc="doWithoutTransaction">
		@Override
		public final <TResult> TResult doNonTransaction(Action<TResult> action)
				throws SQLException, Exception {
			Session session = openSession();
			if (session != null) {
				try {
					TResult result = action.execute(session);
					return result;
				} catch (Exception e) {
					// throw new MyException(getEntityClass(), e);
					throw e;
				} finally {
					session.close();
				}
			} else {
				// throw new MyException(getEntityClass(), new
				// HibernateException(
				// "coudn't initalize session"));
				throw new SQLException(new HibernateException(
						"coudn't initalize session"));
			}
		}

		@Override
		public final void doNonTransaction(ActionWithoutResult action)
				throws SQLException, Exception {
			Session session = openSession();
			if (session != null) {
				try {
					action.execute(session);
				} catch (Exception e) {
					// throw new MyException(getEntityClass(), e);
					throw e;
				} finally {
					session.close();
				}
			} else {
				// throw new MyException(getEntityClass(), new
				// HibernateException(
				// "coudn't initalize session"));
				throw new SQLException(new HibernateException(
						"coudn't initalize session"));
			}
		}// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="doInTransaction">
		@Override
		public final <TResult> TResult doInTransaction(Action<TResult> action)
				throws SQLException, Exception {
			Session session = openSession();
			if (session != null) {
				Transaction tx = session.getTransaction();
				if (tx != null) {
					try {
						tx.begin();
						TResult result = action.execute(session);
						tx.commit();
						return result;
					} catch (Exception e) {
						try {
							tx.rollback();
						} catch (Exception ex) {
							// Logger.getLogger(getClass().getName()).log(
							// Level.SEVERE, null, ex);
						}
						// throw new MyException(getEntityClass(), e);
						throw e;
					} finally {
						session.close();
					}
				} else {
					// throw new MyException(getEntityClass(),
					// new HibernateException("coudn't initalize transaction"));
					throw new SQLException(new HibernateException(
							"coudn't initalize transaction"));
				}
			} else {
				// throw new MyException(getEntityClass(), new
				// HibernateException(
				// "coudn't initalize session"));
				throw new SQLException(new HibernateException(
						"coudn't initalize session"));
			}
		}

		@Override
		public final void doInTransaction(ActionWithoutResult action)
				throws SQLException, Exception {
			Session session = openSession();
			if (session != null) {
				Transaction tx = session.getTransaction();
				if (tx != null) {
					try {
						tx.begin();
						action.execute(session);
						tx.commit();
					} catch (Exception e) {
						try {
							tx.rollback();
						} catch (Exception ex) {
							// Logger.getLogger(getClass().getName()).log(
							// Level.SEVERE, null, ex);
						}
						// throw new MyException(getEntityClass(), e);
						throw e;
					} finally {
						session.close();
					}
				} else {
					// throw new MyException(getEntityClass(),
					// new HibernateException("coudn't initalize transaction"));
					throw new SQLException(new HibernateException(
							"coudn't initalize transaction"));
				}
			} else {
				// throw new MyException(getEntityClass(), new
				// HibernateException(
				// "coudn't initalize session"));
				throw new SQLException(new HibernateException(
						"coudn't initalize session"));
			}
		}// </editor-fold>

		// <editor-fold defaultstate="collapsed" desc="nonTransactionals">

	};

	@SuppressWarnings("unused")
	private <TResult> TResult doInTransaction(Provider provider,
			Action<TResult> action) throws HibernateException {
		try {
			return provider.doInTransaction(action);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HibernateException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
	}

	private void doInTransaction(Provider provider, ActionWithoutResult action)
			throws HibernateException {
		try {
			provider.doInTransaction(action);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HibernateException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
	}

	private <TResult> TResult doNonTransaction(Provider provider,
			Action<TResult> action) throws HibernateException {
		try {
			return provider.doNonTransaction(action);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HibernateException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
	}

	private void doNonTransaction(Provider provider, ActionWithoutResult action)
			throws HibernateException {
		try {
			provider.doNonTransaction(action);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HibernateException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
	}

	@Override
	public final T find(final ID id) {
		T result = doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(Session session) {
				@SuppressWarnings("unchecked")
				T toResult = (T) session.get(getEntityClass(), id);
				if (toResult != null) {
					beforeClose(toResult);
				}
				return toResult;
			}
		});
		return setDefault(result);
	}

	@Override
	public final T find(final DetachedCriteria detachedCriteria) {

		return doNonTransaction(provider, new Action<T>() {

			@Override
			public T execute(Session session) throws SQLException {
				try {
					Criteria criteria = detachedCriteria
							.getExecutableCriteria(session);

					@SuppressWarnings("unchecked")
					T toResult = (T) criteria.uniqueResult();
					if (toResult != null) {
						beforeClose(toResult);
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ session, ex);
				}
			}
		});
	}

	@Override
	public final List<T> findList(final DetachedCriteria detachedCriteria,
			final Integer firstResult, final Integer maxResults) {

		return doNonTransaction(provider, new Action<List<T>>() {

			@Override
			public List<T> execute(Session session) throws SQLException {
				try {
					Criteria criteria = detachedCriteria
							.getExecutableCriteria(session);
					boolean all = maxResults == -1;
					int offset = !all && firstResult == -1 ? 0 : firstResult;
					if (!all) {
						criteria.setMaxResults(maxResults);
						criteria.setFirstResult(offset);
					}
					@SuppressWarnings("unchecked")
					List<T> toResult = (List<T>) criteria.list();
					if (toResult != null) {
						for (T entity : toResult) {
							beforeClose(entity);
						}
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ session, ex);
				}
			}
		});
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
			public T execute(Session session) throws SQLException {
				try {
					Query query;
					if (isNativeQuery) {
						query = session.createSQLQuery(queryString).addEntity(
								getEntityClass());
					} else {
						query = session.createQuery(queryString);
					}

					for (Entry<String, Object> parameter : parameters) {
						if (parameter.getValue().getClass().isArray()) {
							query.setParameterList(parameter.getKey(),
									(Object[]) parameter.getValue());
						} else {
							query.setParameter(parameter.getKey(),
									parameter.getValue());
						}
					}

					@SuppressWarnings("unchecked")
					T toResult = (T) query.uniqueResult();
					if (toResult != null) {
						beforeClose(toResult);
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ session, ex);
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
			public List<T> execute(Session session) throws SQLException {
				try {
					Query query;
					if (isNativeQuery) {
						query = session.createSQLQuery(queryString).addEntity(
								getEntityClass());
					} else {
						query = session.createQuery(queryString);
					}

					for (Entry<String, Object> parameter : parameters) {
						if (parameter.getValue().getClass().isArray()) {
							query.setParameterList(parameter.getKey(),
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
					List<T> toResult = (List<T>) query.list();
					if (toResult != null) {
						for (T entity : toResult) {
							beforeClose(entity);
						}
					}
					return toResult;
				} catch (Exception ex) {
					// throw new MyException(getEntityClass(), ex,
					// "coudn't initialize executableCriteria in session: "
					// + session);
					ex.printStackTrace();
					throw new SQLException(
							"coudn't initialize executableCriteria in session: "
									+ session, ex);
				}
			}
		});
	}

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Transactionals">
	@Override
	public final void save(final T entity) {

		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(Session session) throws SQLException {
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "before SAVE");
				System.out.println("AbstractDao, before save");
				session.saveOrUpdate(entity);
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "after SAVE");
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}

	@Override
	public void update(final Object entity) {
		ActionWithoutResult action = new ActionWithoutResult() {

			@Override
			public void execute(Session session) throws SQLException {
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "before SAVE");
				System.out.println("AbstractDao, before save");
				session.update(entity);
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "after SAVE");
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
			public void execute(Session session) throws SQLException {
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "before DELETE");

				System.out.println("AbstractDao, before delete");
				session.delete(entity);
				// Logger.getLogger(getClass().getName()).log(Level.INFO,
				// "after DELETE");
			}
		};
		if (useTransaction) {
			doInTransaction(provider, action);
		} else {
			doNonTransaction(provider, action);
		}
	}

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="find`s(...)">
	@Override
	public final T find(String propertyName, Object value) {
		return this.find(propertyName, value, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#find(java.lang.String,
	 * java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public T find(String propertyName, Object value, Critirion likeType) {
		return this.find(new String[] { propertyName }, new Object[] { value },
				likeType);
	}

	@Override
	public final T find(String[] propertyNames, Object[] values) {
		return this.find(propertyNames, values, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#find(java.lang.String[],
	 * java.lang.Object[], java.lang.Boolean)
	 */
	@Override
	public T find(String[] propertyNames, Object[] values, Critirion likeType) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (propertyNames != null && values != null && propertyNames.length > 0
				&& propertyNames.length == values.length) {
			for (int i = 0; i < propertyNames.length; i++) {
				map.put(propertyNames[i], values[i]);
			}
		}
		return find(map, likeType);
	}

	@Override
	public final T find(String associationPath, String propertyName,
			Object value) {
		return this.find(associationPath, propertyName, value, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#find(java.lang.String,
	 * java.lang.String, java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public T find(String associationPath, String propertyName, Object value,
			Critirion likeType) {
		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass(),
				"localEntity");
		dc.createAlias(associationPath, "remoteEntity");

		dc.add(Restrictions.eq("remoteEntity." + propertyName, value));

		return find(dc);
	}

	@Override
	public final T find(final Map<String, Object> map) {
		return find(map, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#find(java.util.Map,
	 * java.lang.Boolean)
	 */
	@Override
	public T find(Map<String, Object> map, Critirion likeType) {
		T result = null;
		if (map != null & map.size() > 0) {
			DetachedCriteria criteria = createCriteria();
			if (!Critirion.NONE.equals(likeType)) {
				like(criteria, map, likeType);
			} else {
				criteria.add(Restrictions.allEq(map));
			}
			result = find(criteria);
		}
		return setDefault(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findLikeMap(java.util.Map)
	 */
	@Override
	public T findLikeMap(Map<String, Entry<Object, Critirion>> map) {
		T result = null;
		if (map != null & map.size() > 0) {
			DetachedCriteria criteria = createCriteria();

			likeMap(criteria, map);

			result = find(criteria);
		}
		return setDefault(result);
	}

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="findList`s(...)">
	@Override
	public final List<T> findList() {
		return this.findList(-1, -1);
	}

	@Override
	public final List<T> findList(final Integer firstResult,
			final Integer maxResults) {
		return findList(createCriteria(), firstResult, maxResults);
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
	public final List<T> findList(final String orderBy,
			final Integer firstResult, final Integer maxResults) {
		return this.findList(orderBy, OrderBy.NONE, firstResult, maxResults);
	}

	@Override
	public final List<T> findList(final String orderBy,
			final OrderBy orderByType, final Integer firstResult,
			final Integer maxResults) {
		return findList(createCriteria(orderBy, orderByType), firstResult,
				maxResults);
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value) {
		return this.findList(associationPath, propertyName, value,
				Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.String, java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Critirion likeType) {
		return this.findList(associationPath, propertyName, value, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String associationPath, String propertyName,
			Object value, final Integer firstResult, final Integer maxResults) {
		return this.findList(associationPath, propertyName, value, firstResult,
				maxResults, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.String, java.lang.Object, java.lang.Integer, java.lang.Integer,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String associationPath, String propertyName,
			Object value, Integer firstResult, Integer maxResults,
			Critirion likeType) {

		DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass(),
				"localEntity");
		dc.createAlias(associationPath, "remoteEntity");

		if (!Critirion.NONE.equals(likeType)) {
			SimpleExpression expression = Restrictions.like("remoteEntity."
					+ propertyName, likeType.getLike(value));
			if (likeType.getIgnoreCase()) {
				expression.ignoreCase();
			}
			dc.add(expression);
		} else {
			dc.add(Restrictions.eq("remoteEntity." + propertyName, value));
		}
		return findList(dc, firstResult, maxResults);
	}

	@Override
	public final List<T> findList(String propertyName, Object value) {
		return this.findList(propertyName, value, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value,
			Critirion likeType) {
		return this.findList(propertyName, value, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final Integer firstResult, final Integer maxResults) {

		return this.findList(propertyName, value, firstResult, maxResults,
				Critirion.NONE);
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
			Integer firstResult, Integer maxResults, Critirion likeType) {

		return this.findList(new String[] { propertyName },
				new Object[] { value }, firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final OrderBy orderByType) {

		return this.findList(propertyName, value, orderBy, orderByType,
				Critirion.NONE);
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
			OrderBy orderByType, Critirion likeType) {

		return this.findList(propertyName, value, orderBy, orderByType, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy) {

		return this.findList(propertyName, value, orderBy, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String,
	 * java.lang.Object, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String propertyName, Object value, String orderBy,
			Critirion likeType) {

		return this.findList(propertyName, value, orderBy, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final Integer firstResult,
			final Integer maxResults) {

		return this.findList(propertyName, value, orderBy, firstResult,
				maxResults, Critirion.NONE);
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
			Integer firstResult, Integer maxResults, Critirion likeType) {

		return this.findList(propertyName, value, orderBy, OrderBy.NONE,
				firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String propertyName, Object value,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return this.findList(propertyName, value, orderBy, orderByType,
				firstResult, maxResults, Critirion.NONE);
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
			Critirion likeType) {

		return this.findList(new String[] { propertyName },
				new Object[] { value }, orderBy, orderByType, firstResult,
				maxResults, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values) {
		return findList(propertyNames, values, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			Critirion likeType) {
		return findList(propertyNames, values, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy) {

		return findList(propertyNames, values, orderBy, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.lang.String[],
	 * java.lang.Object[], java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(String[] propertyNames, Object[] values,
			String orderBy, Critirion likeType) {

		return findList(propertyNames, values, orderBy, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType) {

		return findList(propertyNames, values, orderBy, orderByType,
				Critirion.NONE);
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
			String orderBy, OrderBy orderByType, Critirion likeType) {

		return findList(propertyNames, values, orderBy, orderByType, -1, -1,
				likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final Integer firstResult, final Integer maxResults) {

		return findList(propertyNames, values, firstResult, maxResults,
				Critirion.NONE);
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
			Integer firstResult, Integer maxResults, Critirion likeType) {

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
				maxResults, Critirion.NONE);
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
			Critirion likeType) {

		return findList(propertyNames, values, orderBy, OrderBy.NONE,
				firstResult, maxResults, likeType);
	}

	@Override
	public final List<T> findList(String[] propertyNames, Object[] values,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return findList(propertyNames, values, orderBy, orderByType,
				firstResult, maxResults, Critirion.NONE);
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
			Integer maxResults, Critirion likeType) {

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
		return findList(map, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, Critirion likeType) {
		return findList(map, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(final Map<String, Object> map,
			Integer firstResult, Integer maxResults) {
		return findList(map, firstResult, maxResults, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, Integer firstResult,
			Integer maxResults, Critirion likeType) {
		DetachedCriteria criteria = createCriteria();

		if (map != null & !map.isEmpty()) {
			if (!Critirion.NONE.equals(likeType)) {
				like(criteria, map, likeType);
			} else {
				criteria.add(Restrictions.allEq(map));
			}
		}
		return findList(criteria, firstResult, maxResults);
	}

	@Override
	public final List<T> findList(Map<String, Object> map, final String orderBy) {
		return findList(map, orderBy, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Critirion likeType) {
		return findList(map, orderBy, OrderBy.NONE, likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final OrderBy orderByType) {

		return findList(map, orderBy, orderByType, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.String, br.com.abril.pce.client.service.OrderBy,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Critirion likeType) {

		return findList(map, orderBy, orderByType, -1, -1, likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final Integer firstResult,
			final Integer maxResults) {

		return findList(map, orderBy, firstResult, maxResults, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			Integer firstResult, Integer maxResults, Critirion likeType) {

		return findList(map, orderBy, OrderBy.NONE, firstResult, maxResults,
				likeType);
	}

	@Override
	public final List<T> findList(Map<String, Object> map,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {

		return findList(map, orderBy, orderByType, firstResult, maxResults,
				Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findList(java.util.Map,
	 * java.lang.String, br.com.abril.pce.client.service.OrderBy,
	 * java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public List<T> findList(Map<String, Object> map, String orderBy,
			OrderBy orderByType, Integer firstResult, Integer maxResults,
			Critirion likeType) {

		DetachedCriteria criteria = createCriteria(orderBy, orderByType);
		if (map != null & !map.isEmpty()) {
			if (!Critirion.NONE.equals(likeType)) {
				like(criteria, map, likeType);
			} else {
				criteria.add(Restrictions.allEq(map));
			}
		}
		return findList(criteria, firstResult, maxResults);
	}

	// <editor-fold defaultstate="collapsed" desc="findList(Criteria criteria)">

	@Override
	public final List<T> findList(final DetachedCriteria dc) {
		return findList(dc, -1, -1);
	}

	@Override
	public final List<T> findList(final DetachedCriteria dc,
			final String orderBy) {
		return findList(dc, orderBy, OrderBy.NONE, -1, -1);
	}

	@Override
	public final List<T> findList(final DetachedCriteria dc,
			final String orderBy, final OrderBy orderByType) {
		return findList(dc, orderBy, orderByType, -1, -1);
	}

	@Override
	public final List<T> findList(final DetachedCriteria dc,
			final String orderBy, final OrderBy orderByType,
			final Integer firstResult, final Integer maxResults) {
		return findList(addOrder(dc, orderBy, orderByType), firstResult,
				maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map) {
		return findListLikeMap(map, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			Integer firstResult, Integer maxResults) {

		DetachedCriteria criteria = createCriteria();

		likeMap(criteria, map);

		return findList(criteria, firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.String)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy) {

		return findListLikeMap(map, orderBy, OrderBy.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.String, br.com.abril.pce.client.service.OrderBy)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType) {

		return findListLikeMap(map, orderBy, orderByType, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, Integer firstResult, Integer maxResults) {

		return findListLikeMap(map, orderBy, OrderBy.NONE, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#findListLikeMap(java.util.Map,
	 * java.lang.String, br.com.abril.pce.client.service.OrderBy,
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<T> findListLikeMap(Map<String, Entry<Object, Critirion>> map,
			String orderBy, OrderBy orderByType, Integer firstResult,
			Integer maxResults) {

		DetachedCriteria criteria = createCriteria(orderBy, orderByType);

		likeMap(criteria, map);

		return findList(criteria, firstResult, maxResults);
	}

	// </editor-fold>
	// </editor-fold>

	@Override
	public final Long getListCount() {
		return getListCount(createCriteria());
	}

	@Override
	public final Long getListCount(String propertyName, Object value) {
		return this.getListCount(propertyName, value, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#getListCount(java.lang.String,
	 * java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public Long getListCount(String propertyName, Object value,
			Critirion likeType) {
		return this.getListCount(new String[] { propertyName },
				new Object[] { value }, likeType);
	}

	@Override
	public final Long getListCount(String[] propertyNames, Object[] values) {
		return this.getListCount(propertyNames, values, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#getListCount(java.lang.String[],
	 * java.lang.Object[], java.lang.Boolean)
	 */
	@Override
	public Long getListCount(String[] propertyNames, Object[] values,
			Critirion likeType) {

		Map<String, Object> map = map(propertyNames, values);

		return getListCount(map, likeType);
	}

	@Override
	public final Long getListCount(Map<String, Object> map) {

		return getListCount(map, Critirion.NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#getListCount(java.util.Map,
	 * java.lang.Boolean)
	 */
	@Override
	public Long getListCount(Map<String, Object> map, Critirion likeType) {

		DetachedCriteria criteria = createCriteria();
		if (map != null & !map.isEmpty()) {
			if (!Critirion.NONE.equals(likeType)) {
				like(criteria, map, likeType);
			} else {
				criteria.add(Restrictions.allEq(map));
			}
		}
		return getListCount(criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.pce.repository.Dao#getListCountLikeMap(java.util.Map,
	 * java.lang.Boolean)
	 */
	@Override
	public Long getListCountLikeMap(Map<String, Entry<Object, Critirion>> map) {

		DetachedCriteria criteria = createCriteria();

		likeMap(criteria, map);

		return getListCount(criteria);
	}

	@Override
	public final Long getListCount(final DetachedCriteria dc) {

		return doNonTransaction(provider, new Action<Long>() {

			@Override
			public Long execute(Session session) {

				dc.setProjection(Projections.rowCount());

				Criteria criteria = dc.getExecutableCriteria(session);

				return ParserUtils.parseLong(criteria.uniqueResult());
			}
		});
	}

	// <editor-fold defaultstate="collapsed" desc="createCriteria">
	protected final DetachedCriteria createCriteria(String orderBy,
			OrderBy orderByType) {
		DetachedCriteria dc = this.createCriteria();
		return addOrder(dc, orderBy, orderByType);
	}

	protected final DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(this.getEntityClass());
	}// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="addOrder">
	protected final DetachedCriteria addOrder(DetachedCriteria dc,
			String orderBy) {
		return addOrder(dc, orderBy, OrderBy.DESC);
	}

	protected final DetachedCriteria addOrder(DetachedCriteria dc,
			String orderBy, OrderBy orderByType) {

		if (!StringUtils.isNullOrEmpty(orderBy)) {
			if (orderByType == OrderBy.ASC) {
				dc.addOrder(Order.asc(orderBy));
			} else if (orderByType == OrderBy.DESC) {
				dc.addOrder(Order.desc(orderBy));
			}
		}
		return dc;
	}// </editor-fold>

	private T setDefault(T entity) {
		if (entity == null) {
			try {
				entity = this.getEntityClass().newInstance();
			} catch (InstantiationException ex) {
				// Logger.getLogger(getClass().getName()).log(
				// Level.SEVERE,
				// "Can't found default contructor for class:  "
				// + this.getEntityClass(), ex);
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				// Logger.getLogger(getClass().getName()).log(
				// Level.SEVERE,
				// "Can't found default contructor for class:  "
				// + this.getEntityClass(), ex);
				ex.printStackTrace();
			}
		}
		return entity;
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

	private void like(DetachedCriteria criteria, Map<String, Object> map,
			Critirion likeType) {

		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			System.out.println("put a like, key: " + entry.getKey()
					+ ", value: " + entry.getValue());

			SimpleExpression expression = Restrictions.like(entry.getKey(),
					likeType.getLike(entry.getValue()));
			if (likeType.getIgnoreCase()) {
				expression.ignoreCase();
			}
			criteria.add(expression);
		}
	}

	private void likeMap(DetachedCriteria criteria,
			Map<String, Entry<Object, Critirion>> map) {

		Iterator<Entry<String, Entry<Object, Critirion>>> iterator = map
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Entry<Object, Critirion>> entryMap = iterator.next();

			Entry<Object, Critirion> entry = entryMap.getValue();

			if (!Critirion.NONE.equals(entry.getValue())) {
				System.out.println("put a like, key: " + entry.getKey()
						+ ", value: " + entry.getValue());

				Critirion likeType = entry.getValue();
				SimpleExpression expression = Restrictions.like(
						entryMap.getKey(), likeType.getLike(entry.getValue()));
				if (likeType.getIgnoreCase()) {
					expression.ignoreCase();
				}
				criteria.add(expression);
			} else {
				System.out.println("put a eq, key: " + entry.getKey()
						+ ", value: " + entry.getValue());
				criteria.add(Restrictions.eq(entryMap.getKey(),
						entry.getValue()));
			}
		}
	}
}
