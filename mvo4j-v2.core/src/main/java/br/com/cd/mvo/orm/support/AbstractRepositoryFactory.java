package br.com.cd.mvo.orm.support;

import java.lang.annotation.Annotation;

import org.apache.commons.collections.map.ReferenceMap;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.orm.ListenableRepository;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.util.GenericsUtils;

public abstract class AbstractRepositoryFactory<F, B, R extends ListenableRepository<?>>
		implements RepositoryFactory<F, B, R> {

	private final Class<? extends Annotation> entityIdentifierAnnotation;
	private final Class<? extends Annotation> entityAnnotation;

	private static final String BEAN_NAME_PREFIX = AbstractRepositoryFactory.class
			.getName();

	protected final Container container;
	protected final Class<F> persistenceManagerFactoryType;

	protected final Class<B> persistenceManagerType;

	@SuppressWarnings("unchecked")
	public AbstractRepositoryFactory(Container container,
			Class<? extends Annotation> persistenceTypeAnnotation,
			Class<? extends Annotation> persistenceIdentifierAnnotation) {
		this.container = container;
		this.entityAnnotation = persistenceTypeAnnotation;
		this.entityIdentifierAnnotation = persistenceIdentifierAnnotation;

		this.persistenceManagerFactoryType = GenericsUtils.getTypesFor(
				this.getClass()).get(0);
		this.persistenceManagerType = GenericsUtils
				.getTypesFor(this.getClass()).get(1);
	}

	protected abstract B createPersistenceManager(F factory)
			throws NoSuchBeanDefinitionException;

	private ReferenceMap cachedBean = new ReferenceMap();

	@Override
	@SuppressWarnings("unchecked")
	public final B getPersistenceManager(
			String persistenceManagerFactoryBeanName)
			throws NoSuchBeanDefinitionException {

		if (!cachedBean.containsKey(persistenceManagerFactoryBeanName)) {
			synchronized (this) {
				if (!cachedBean.containsKey(persistenceManagerFactoryBeanName)) {
					B persistenceManager = this
							.createPersistenceManager(container.getBean(
									persistenceManagerFactoryBeanName,
									persistenceManagerFactoryType));
					cachedBean.put(persistenceManagerFactoryBeanName,
							persistenceManager);
					return persistenceManager;
				}
			}
		}
		return (B) cachedBean.get(persistenceManagerFactoryBeanName);
	}

	@Override
	public Class<B> getPersistenceManagagerType() {
		return persistenceManagerType;
	}

	@Override
	public Class<? extends Annotation> getEntityAnnotation() {
		return entityAnnotation;
	}

	@Override
	public Class<? extends Annotation> getEntityIdentifierAnnotation() {
		return entityIdentifierAnnotation;
	}

	public static String getBeanName(String persistenceManagerBeanName) {

		return BEAN_NAME_PREFIX + "." + persistenceManagerBeanName;
	}
}
