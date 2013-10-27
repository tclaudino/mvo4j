package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.util.javassist.JavassistUtils;

public abstract class AbstractBeanMetaDataFactory<M extends BeanMetaData, A extends Annotation>
		implements BeanMetaDataFactory<M, A> {

	protected final Class<? extends BeanObject> beanType;
	protected final Class<M> metaDataType;
	protected final Class<A> annotationType;

	@SuppressWarnings("unchecked")
	public AbstractBeanMetaDataFactory(Class<? extends BeanObject> beanType) {
		this.beanType = beanType;
		this.metaDataType = GenericsUtils.getTypesFor(this.getClass()).get(0);
		this.annotationType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	@Override
	public Class<? extends BeanObject> getBeanObjectType() {
		return beanType;
	}

	@Override
	public Class<M> getBeanMetaDataType() {
		return metaDataType;
	}

	@Override
	public Class<A> getBeanAnnotationType() {
		return annotationType;
	}

	@Override
	public WriteablePropertyMap newDefaultPropertyMap(Container container) {

		WriteablePropertyMap propertyMap = new WriteablePropertyMap();
		propertyMap.add(BeanMetaData.INITIAL_PAGE_SIZE, container
				.getApplicationConfig().getInitialPageSize());
		propertyMap.add(BeanMetaData.MESSAGE_BUNDLE, container
				.getApplicationConfig().getBundleName());
		propertyMap.add(BeanMetaData.SCOPE, container.getApplicationConfig()
				.getDefaultScopeName());
		propertyMap.add(BeanMetaData.PERSISTENCE_FACTORY_QUALIFIER, container
				.getApplicationConfig().getPersistenceManagerFactoryBeanName());
		propertyMap.add(BeanMetaData.PERSISTENCE_PROVIDER, container
				.getApplicationConfig().getRepositoryFactoryClass());

		return propertyMap;
	}

	protected abstract M doCreateBeanMetaData(WriteablePropertyMap propertyMap);

	@Override
	public BeanMetaDataWrapper<M> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container, boolean readAnnotationAttributes)
			throws ConfigurationException {

		M beanMetaData;
		if (readAnnotationAttributes)
			beanMetaData = this.createBeanMetaData(propertyMap, beanType,
					beanType.getAnnotation(this.annotationType));
		else
			beanMetaData = this.doCreateBeanMetaData(propertyMap);

		BeanMetaDataWrapper<M> metaDataWrapper = new BeanMetaDataWrapper<M>(
				beanType, beanMetaData);

		propertyMap.add(BeanMetaData.NAME,
				BeanMetaDataWrapper.generateBeanName(metaDataWrapper));

		return metaDataWrapper;
	}

	protected M createBeanMetaData(WriteablePropertyMap map, Class<?> beanType,
			A annotation) throws ConfigurationException {

		try {
			map.addAll(JavassistUtils.getAnnotationAtributes(beanType,
					annotation));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		return this.doCreateBeanMetaData(map);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		return scanner.scan(this.annotationType, packagesToScan);
	}

}
