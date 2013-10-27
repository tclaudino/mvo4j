package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public interface BeanMetaDataFactory<M extends BeanMetaData, A extends Annotation> {

	Class<? extends BeanObject> getBeanObjectType();

	Class<A> getBeanAnnotationType();

	WriteablePropertyMap newDefaultPropertyMap(Container container);

	BeanMetaDataWrapper<M> createBeanMetaData(WriteablePropertyMap propertyMap,
			Class<?> beanType, Container container,
			boolean readAnnotationAttributes) throws ConfigurationException;

	Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan);

	Class<M> getBeanMetaDataType();
}