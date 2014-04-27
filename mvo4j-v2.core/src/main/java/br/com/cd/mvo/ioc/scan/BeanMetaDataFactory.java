package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.ContainerConfig;

@SuppressWarnings("rawtypes")
public interface BeanMetaDataFactory<M extends DefaultBeanMetaData, A extends Annotation> {

	Class<?> getBeanObjectType();

	Class<A> getBeanAnnotationType();

	WriteableMetaData newDefaultPropertyMap(ContainerConfig containerConfig);

	BeanMetaDataWrapper<M> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType, boolean readAnnotationAttributes) throws ConfigurationException;

	Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan);

	Class<M> getBeanMetaDataType();
}