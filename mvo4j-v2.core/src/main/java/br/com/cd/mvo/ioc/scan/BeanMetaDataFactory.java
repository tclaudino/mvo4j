package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;

@SuppressWarnings("rawtypes")
public interface BeanMetaDataFactory<D extends BeanMetaData, A extends Annotation>
		extends Comparable<BeanMetaDataFactory> {

	Class<? extends BeanObject> getBeanType();

	Class<A> getBeanAnnotationType();

	D createBeanMetaData(WriteablePropertyMap propertyMap);

	D createBeanMetaData(Class<?> beanType, A annotation)
			throws ConfigurationException;

	int getOrder();
}