package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.ApplicationConfig;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.DefaultBeanMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.util.javassist.JavassistUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractBeanMetaDataFactory<M extends DefaultBeanMetaData, A extends Annotation> implements BeanMetaDataFactory<M, A> {

	protected final Class<?> beanType;
	protected final Class<M> metaDataType;
	protected final Class<A> annotationType;

	@SuppressWarnings("unchecked")
	public AbstractBeanMetaDataFactory(Class<?> beanType) {
		this.beanType = beanType;
		this.metaDataType = GenericsUtils.getTypesFor(this.getClass()).get(0);
		this.annotationType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	@Override
	public Class<?> getBeanObjectType() {
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
	public WriteableMetaData newDefaultPropertyMap(ApplicationConfig applicationConfig) {

		WriteableMetaData propertyMap = new WriteableMetaData();
		propertyMap.add(BeanMetaData.INITIAL_PAGE_SIZE, applicationConfig.getInitialPageSize());
		propertyMap.add(BeanMetaData.MESSAGE_BUNDLE, applicationConfig.getBundleName());
		propertyMap.add(BeanMetaData.SCOPE, applicationConfig.getDefaultScopeName());
		propertyMap.add(BeanMetaData.PERSISTENCE_FACTORY_QUALIFIER, applicationConfig.getPersistenceManagerFactoryBeanName());
		propertyMap.add(BeanMetaData.PERSISTENCE_PROVIDER, applicationConfig.getRepositoryFactoryClass());

		return propertyMap;
	}

	protected abstract M doCreateBeanMetaData(WriteableMetaData propertyMap);

	@Override
	public BeanMetaDataWrapper<M> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType, boolean readAnnotationAttributes)
			throws ConfigurationException {

		M beanMetaData;
		if (readAnnotationAttributes)
			propertyMap = this.readAnnotationMetaData(propertyMap, beanType, beanType.getAnnotation(this.annotationType));

		String scope = propertyMap.get(BeanMetaData.SCOPE);
		beanMetaData = this.doCreateBeanMetaData(propertyMap);
		if (!scope.isEmpty()) propertyMap.add(BeanMetaData.SCOPE, scope);

		BeanMetaDataWrapper<M> metaDataWrapper = new BeanMetaDataWrapper<M>(beanType, beanMetaData);

		propertyMap.add(BeanMetaData.NAME, BeanMetaDataWrapper.generateBeanName(metaDataWrapper));

		return metaDataWrapper;
	}

	protected WriteableMetaData readAnnotationMetaData(WriteableMetaData map, Class<?> beanType, A annotation)
			throws ConfigurationException {

		try {
			map.addAll(JavassistUtils.getAnnotationAtributes(beanType, annotation));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

		return map;
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		return scanner.scan(this.annotationType, packagesToScan);
	}

	@Override
	public String toString() {
		return "BeanMetaDataFactory [beanType=" + beanType + ", metaDataType=" + metaDataType + ", annotationType=" + annotationType + "]";
	}

}
