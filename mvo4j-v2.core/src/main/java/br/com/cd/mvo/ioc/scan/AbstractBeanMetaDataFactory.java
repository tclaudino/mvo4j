package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.util.GenericsUtils;
import br.com.cd.util.javassist.JavassistUtils;

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

	@SuppressWarnings("unchecked")
	@Override
	public WriteableMetaData newDefaultPropertyMap(ContainerConfig containerConfig) {

		WriteableMetaData propertyMap = new WriteableMetaData();
		propertyMap.add(BeanMetaData.INITIAL_PAGE_SIZE,
				containerConfig.getInitParameter(ConfigParamKeys.INITIAL_PAGESIZE, ConfigParamKeys.DefaultValues.INITIAL_PAGESIZE));

		propertyMap.add(BeanMetaData.MESSAGE_BUNDLE_NAME,
				containerConfig.getInitParameter(ConfigParamKeys.MESSAGE_BUNDLE_NAME, ConfigParamKeys.DefaultValues.MESSAGE_BUNDLE_NAME));

		propertyMap.add(BeanMetaData.SCOPE_NAME,
				containerConfig.getInitParameter(ConfigParamKeys.SCOPE_NAME_DEFAULT, ConfigParamKeys.DefaultValues.SCOPE_NAME_DEFAULT));
		propertyMap.add(BeanMetaData.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME, containerConfig.getInitParameter(
				ConfigParamKeys.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME, ConfigParamKeys.DefaultValues.PERSISTENCE_MANAGER_FACTORY_BEAN_NAME));
		propertyMap.add(BeanMetaData.REPOSITORY_FACTORY_CLASS,
				containerConfig.getInitParameter(ConfigParamKeys.REPOSITORY_FACTORY_CLASS, ConfigParamKeys.DefaultValues.REPOSITORY_FACTORY_CLASS));

		return propertyMap;
	}

	protected abstract M doCreateBeanMetaData(WriteableMetaData propertyMap);

	@Override
	public BeanMetaDataWrapper<M> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType, boolean readAnnotationAttributes)
			throws ConfigurationException {

		M beanMetaData;
		if (readAnnotationAttributes)
			propertyMap = this.readAnnotationMetaData(propertyMap, beanType, beanType.getAnnotation(this.annotationType));

		String scope = propertyMap.get(BeanMetaData.SCOPE_NAME);
		beanMetaData = this.doCreateBeanMetaData(propertyMap);
		if (!scope.isEmpty())
			propertyMap.add(BeanMetaData.SCOPE_NAME, scope);

		BeanMetaDataWrapper<M> metaDataWrapper = new BeanMetaDataWrapper<M>(beanType, beanMetaData);

		propertyMap.add(BeanMetaData.NAME, BeanMetaDataWrapper.generateBeanName(metaDataWrapper));

		return metaDataWrapper;
	}

	protected WriteableMetaData readAnnotationMetaData(WriteableMetaData map, Class<?> beanType, A annotation) throws ConfigurationException {

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
