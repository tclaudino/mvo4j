package br.com.cd.mvo.ioc;

import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.core.Listenable;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.util.GenericsUtils;
import br.com.cd.util.StringUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractBeanFactory<D extends DefaultBeanMetaData, A extends Annotation> implements BeanFactory<D, A> {

	protected Logger looger = Logger.getLogger(this.getClass());

	protected final Container container;
	protected final Class<D> metaDataType;
	protected final Class<A> annotationType;
	protected final BeanMetaDataFactory<D, A> metaDataFactory;

	@SuppressWarnings({ "unchecked" })
	public AbstractBeanFactory(Container container, BeanMetaDataFactory<D, A> metaDataFactory) {
		assert container != null : "'container' must be not null";
		assert metaDataFactory != null : "'metaDataFactory' must be not null";
		this.container = container;
		this.metaDataFactory = metaDataFactory;

		List<Class> typesFor = GenericsUtils.getTypesFor(this.getClass());
		this.metaDataType = typesFor.get(0);
		this.annotationType = typesFor.get(1);
		assert this.metaDataType != null : "generic parameter[0] <D extends BeanMetaData> must be not null";
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

	@Override
	public void postConstruct(final BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper) {

		looger.debug(StringUtils.format("postConstruct bean '{0}', adding listeners...", bean));

		if (!(bean instanceof Listenable))
			return;

		((Listenable) bean).postConstruct();
	}

	@Override
	public String toString() {
		return "BeanFactory [metaDataType=" + metaDataType + ", annotationType=" + annotationType + ", metaDataFactory=" + metaDataFactory
				+ "]";
	}

}