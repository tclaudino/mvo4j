package br.com.cd.mvo.ioc;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;

public interface BeanFactory<D extends BeanMetaData, A extends Annotation> {

	boolean isCandidate(BeanMetaData metaData);

	boolean isCandidate(Class<? extends Annotation> annotation);

	BeanObject getInstance(BeanMetaDataWrapper<D> metaDataWrapper)
			throws ConfigurationException;

	BeanMetaDataFactory<D, A> getBeanMetaDataFactory();

	Class<D> getBeanMetaDataType();

	BeanObject wrap(BeanObject bean);

}