package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

@SuppressWarnings("rawtypes")
public interface BeanMetaDataFactory<M extends BeanMetaData, A extends Annotation>
		extends Comparable<BeanMetaDataFactory> {

	Class<? extends BeanObject> getBeanType();

	Class<A> getBeanAnnotationType();

	int getOrder();

	WriteablePropertyMap newDefaultPropertyMap(Container container);

	BeanMetaDataWrapper<M> createBeanMetaData(WriteablePropertyMap propertyMap);

	BeanMetaDataWrapper<M> createBeanMetaData(WriteablePropertyMap propertyMap,
			Class<?> beanType, Container container)
			throws ConfigurationException;

	Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan);
}