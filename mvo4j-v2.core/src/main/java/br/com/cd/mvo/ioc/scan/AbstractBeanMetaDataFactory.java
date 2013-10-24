package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.BeanFactoryUtils;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.util.ProxyUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractBeanMetaDataFactory<M extends BeanMetaData, A extends Annotation>
		implements BeanMetaDataFactory<M, A> {

	protected final Class<? extends BeanObject> beanType;
	protected final Class<A> annotationType;

	@SuppressWarnings("unchecked")
	public AbstractBeanMetaDataFactory(Class<? extends BeanObject> beanType) {
		this.beanType = beanType;
		this.annotationType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	@Override
	public Class<? extends BeanObject> getBeanType() {
		return beanType;
	}

	@Override
	public Class<A> getBeanAnnotationType() {
		return annotationType;
	}

	@Override
	public WriteablePropertyMap newDefaultPropertyMap(Container container) {

		WriteablePropertyMap propertyMap = new WriteablePropertyMap();
		propertyMap.add(BeanMetaData.INITIAL_PAGE_SIZE, container
				.getInitApplicationConfig().getInitialPageSize());
		propertyMap.add(BeanMetaData.MESSAGE_BUNDLE, container
				.getInitApplicationConfig().getBundleName());
		propertyMap.add(BeanMetaData.SCOPE, container
				.getInitApplicationConfig().getScopeName());
		propertyMap.add(BeanMetaData.PERSISTENCE_FACTORY_QUALIFIER, container
				.getInitApplicationConfig().getPersistenceFactoryQualifier());
		propertyMap
				.add(BeanMetaData.PERSISTENCE_PROVIDER, container
						.getInitApplicationConfig()
						.getPersistenceManagerFactoryClass());

		return propertyMap;
	}

	protected abstract M doCreateBeanMetaData(WriteablePropertyMap propertyMap);

	@Override
	public BeanMetaDataWrapper<M> createBeanMetaData(
			WriteablePropertyMap propertyMap) {

		M beanMetaData = this.doCreateBeanMetaData(propertyMap);

		BeanMetaDataWrapper<M> metaDataWrapper = new BeanMetaDataWrapper<M>(
				null, beanMetaData);

		propertyMap.add(BeanMetaData.NAME,
				BeanFactoryUtils.generateBeanName(metaDataWrapper));

		return metaDataWrapper;
	}

	@Override
	public BeanMetaDataWrapper<M> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container) throws ConfigurationException {

		M beanMetaData = this.createBeanMetaData(propertyMap, beanType,
				beanType.getAnnotation(this.annotationType));

		BeanMetaDataWrapper<M> metaDataWrapper = new BeanMetaDataWrapper<M>(
				beanType, beanMetaData);

		propertyMap.add(BeanMetaData.NAME,
				BeanFactoryUtils.generateBeanName(metaDataWrapper));

		return metaDataWrapper;
	}

	protected M createBeanMetaData(WriteablePropertyMap map, Class<?> beanType,
			A annotation) throws ConfigurationException {

		try {
			map.addAll(ProxyUtils.getAnnotationAtributes(beanType, annotation));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		return this.doCreateBeanMetaData(map);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		return scanner.scan(this.annotationType, packagesToScan);
	}

	@Override
	public int compareTo(BeanMetaDataFactory o) {

		return new Integer(this.getOrder())
				.compareTo(new Integer(o.getOrder()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotationType == null) ? 0 : annotationType.hashCode());
		result = prime * result
				+ ((beanType == null) ? 0 : beanType.hashCode());
		result = prime * result + new Integer(this.getOrder()).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanMetaDataFactory other = (BeanMetaDataFactory) obj;
		if (annotationType == null) {
			if (other.getBeanAnnotationType() != null)
				return false;
		} else if (!annotationType.equals(other.getBeanAnnotationType()))
			return false;
		if (beanType == null) {
			if (other.getBeanType() != null)
				return false;
		} else if (!beanType.equals(other.getBeanType()))
			return false;
		if (this.getOrder() != other.getOrder())
			return false;

		return true;
	}

}
