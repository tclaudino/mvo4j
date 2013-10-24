package br.com.cd.mvo.ioc;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;

public interface BeanFactory<D extends BeanMetaData, A extends Annotation> {

	boolean isCandidate(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper);

	boolean isCandidate(Class<? extends Annotation> annotation);

	Class<BeanObject> createProxy(
			BeanMetaDataWrapper<? extends BeanMetaData> beanConfig)
			throws NoSuchBeanDefinitionException;

	boolean isSingleton();

	BeanObject getInstance(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws ConfigurationException;

	Class<D> getBeanMetaDataType();

}