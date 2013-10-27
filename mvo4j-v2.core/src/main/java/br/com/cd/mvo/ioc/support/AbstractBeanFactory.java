package br.com.cd.mvo.ioc.support;

import java.lang.annotation.Annotation;
import java.util.List;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.mvo.util.GenericsUtils;

public abstract class AbstractBeanFactory<D extends BeanMetaData, A extends Annotation>
		implements BeanFactory<D, A> {

	protected final Container container;
	protected final Class<D> metaDataType;
	protected final Class<A> annotationType;
	protected final BeanMetaDataFactory<D, A> metaDataFactory;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractBeanFactory(Container container,
			BeanMetaDataFactory<D, A> metaDataFactory) {
		this.container = container;
		assert this.container != null : "'container' must be not null";
		this.metaDataFactory = metaDataFactory;
		assert this.metaDataFactory != null : "'metaDataFactory' must be not null";
		List<Class> typesFor = GenericsUtils.getTypesFor(this.getClass());
		this.metaDataType = typesFor.get(0);
		assert this.metaDataType != null : "generic parameter[0] <D extends BeanMetaData> must be not null";
		this.annotationType = typesFor.get(1);
		assert this.annotationType != null : "generic parameter[0] <A extends Annotation> must be not null";
	}

	@Override
	public boolean isCandidate(BeanMetaData metaData) {
		return metaDataType.equals(metaData.getClass());
	}

	@Override
	public boolean isCandidate(Class<? extends Annotation> annotation) {
		return this.annotationType.equals(annotation);
	}

	@Override
	public BeanMetaDataFactory<D, A> getBeanMetaDataFactory() {

		return metaDataFactory;
	}

	@Override
	public Class<D> getBeanMetaDataType() {
		return metaDataType;
	}
}