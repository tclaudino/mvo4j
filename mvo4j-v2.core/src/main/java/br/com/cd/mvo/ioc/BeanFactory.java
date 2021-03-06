package br.com.cd.mvo.ioc;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;

@SuppressWarnings("rawtypes")
public interface BeanFactory<M extends DefaultBeanMetaData, A extends Annotation> {

	boolean isCandidate(BeanMetaData metaData);

	boolean isCandidate(Class<? extends Annotation> annotation);

	BeanObject<?> getInstance(M metaData) throws ConfigurationException;

	BeanMetaDataFactory<M, A> getBeanMetaDataFactory();

	Class<M> getBeanMetaDataType();

	void postConstruct(BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper);

	MethodInvokeCallback proxify(BeanObject<?> bean, M metaData) throws ConfigurationException;

}