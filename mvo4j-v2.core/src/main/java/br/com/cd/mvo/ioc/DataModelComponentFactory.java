package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultDataModelFactory;

public class DataModelComponentFactory extends AbstractComponentFactory<DataModelFactory> {

	public DataModelComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected DataModelFactory getInstanceInternal() throws NoSuchBeanDefinitionException {

		return new DefaultDataModelFactory();
	}

	@Override
	protected String getComponentBeanName() {
		return DataModelFactory.BEAN_NAME;
	}
}
