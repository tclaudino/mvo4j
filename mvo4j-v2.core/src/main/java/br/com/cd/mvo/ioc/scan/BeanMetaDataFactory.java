package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.ApplicationConfig;
import br.com.cd.mvo.bean.config.DefaultBeanMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;

@SuppressWarnings("rawtypes")
public interface BeanMetaDataFactory<M extends DefaultBeanMetaData, A extends Annotation> {

	Class<?> getBeanObjectType();

	Class<A> getBeanAnnotationType();

	WriteableMetaData newDefaultPropertyMap(ApplicationConfig applicationConfig);

	BeanMetaDataWrapper<M> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType, boolean readAnnotationAttributes)
			throws ConfigurationException;

	Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan);

	Class<M> getBeanMetaDataType();
}