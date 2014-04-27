package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.mvo.ioc.scan.SubTypeScan;

public class NullInstanceBeanFactory<M extends DefaultBeanMetaData<?>> extends AbstractBeanFactory<M, SubTypeScan> {

	public NullInstanceBeanFactory(Container container, BeanMetaDataFactory<M, SubTypeScan> metaDataFactory) {
		super(container, metaDataFactory);
	}

	@Override
	public BeanObject<?> getInstance(M metaData) throws ConfigurationException {

		throw new UnsupportedOperationException();
	}

	@Override
	public void postConstruct(BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper) {

		// only to prevent super;
	}

	@Override
	public MethodInvokeCallback proxify(BeanObject<?> bean, M metaData) throws ConfigurationException {

		throw new UnsupportedOperationException();
	}

}
