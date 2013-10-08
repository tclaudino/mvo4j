package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultDataModelFactory;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;

public class DataModelComponentFactory extends
		AbstractComponentFactory<DataModelFactory> {

	public DataModelComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected DataModelFactory getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		return new DefaultDataModelFactory();
	}
}
