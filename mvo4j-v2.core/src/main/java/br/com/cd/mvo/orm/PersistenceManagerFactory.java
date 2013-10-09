package br.com.cd.mvo.orm;

import java.lang.annotation.Annotation;

import org.apache.commons.collections.map.ReferenceMap;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public abstract class PersistenceManagerFactory<F, B, R extends Repository<?>> {

	private final Class<? extends Annotation> persistenceIdentifierAnnotation;
	private final Class<? extends Annotation> petPersistenceTypeAnnotation;

	private static final String BEAN_NAME_PREFIX = PersistenceManagerFactory.class
			.getName();

	protected final Container container;
	protected final Class<F> factoryType;

		protected final Class<B> beanType;

	@SuppressWarnings("unchecked")
	public PersistenceManagerFactory(Container container,
			Class<? extends Annotation> persistenceIdentifierAnnotation,
			Class<? extends Annotation> petPersistenceTypeAnnotation) {
		this.container = container;
		this.persistenceIdentifierAnnotation = persistenceIdentifierAnnotation;
		this.petPersistenceTypeAnnotation = petPersistenceTypeAnnotation;

		this.factoryType = GenericsUtils.getTypesFor(this.getClass()).get(0);
		this.beanType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	protected abstract B getInstance(F factory)
			throws NoSuchBeanDefinitionException;

	public abstract Repository<?> getRepositoryInstance(
			String persistenceManagerQualifier, Class<?> entityClass);

	private ReferenceMap cachedBean = new ReferenceMap();

	@SuppressWarnings("unchecked")
	public final B getInstance(String persistenceManagerQualifier)
			throws NoSuchBeanDefinitionException {

		if (!cachedBean.containsKey(persistenceManagerQualifier)) {
			synchronized (this) {
				if (!cachedBean.containsKey(persistenceManagerQualifier)) {
					B instance = this.getInstance(container.getBean(
							persistenceManagerQualifier, factoryType));
					cachedBean.put(persistenceManagerQualifier, instance);
					return instance;
				}
			}
		}
		return (B) cachedBean.get(persistenceManagerQualifier);
	}

	public Class<B> getObjectType() {
		return beanType;
	}

	public Class<? extends Annotation> getPersistenceIdentifierAnnotation() {
		return persistenceIdentifierAnnotation;
	}

	public Class<? extends Annotation> getPersistenceTypeAnnotation() {
		return petPersistenceTypeAnnotation;
	}

	public static String getBeanName(String persistenceManagerQualifier) {

		return BEAN_NAME_PREFIX + "." + persistenceManagerQualifier;
	}
}
