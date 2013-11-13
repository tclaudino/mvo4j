package br.com.cd.mvo.core;

import br.com.cd.mvo.bean.config.BeanMetaData;

public interface BeanObject<T> {

	BeanMetaData<T> getBeanMetaData();
}
