package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.util.GenericsUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractBeanMetaDataFactory<C extends BeanMetaData, A extends Annotation>
		implements BeanMetaDataFactory<C, A> {

	protected final Class<? extends BeanObject> beanType;
	protected final Class<A> annotationType;

	@SuppressWarnings("unchecked")
	public AbstractBeanMetaDataFactory(Class<? extends BeanObject> beanType) {
		this.beanType = beanType;
		this.annotationType = GenericsUtils.getTypesFor(this.getClass()).get(1);
	}

	@Override
	public Class<? extends BeanObject> getBeanType() {
		return beanType;
	}

	@Override
	public Class<A> getBeanAnnotationType() {
		return annotationType;
	}

	@Override
	public int compareTo(BeanMetaDataFactory o) {
		if (this.getOrder() < o.getOrder()) {
			return -1;
		}
		if (this.getOrder() > o.getOrder()) {
			return 1;
		}
		return 0;
	}

}
