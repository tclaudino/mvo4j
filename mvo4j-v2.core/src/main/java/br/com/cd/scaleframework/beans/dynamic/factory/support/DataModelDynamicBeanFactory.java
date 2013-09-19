package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.controller.support.DefaultDataModelFactory;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class DataModelDynamicBeanFactory extends
		AbstractDynamicBeanFactory<DataModelFactory> {

	public DataModelDynamicBeanFactory(ComponentFactoryContainer container) {
		super(container);
	}

	@Override
	public Class<DataModelFactory> getObjectType() {
		return DataModelFactory.class;
	}

	@Override
	protected DataModelFactory getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		return new DefaultDataModelFactory();
	}

}
