package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ControllerBean;
import br.com.cd.mvo.core.ControllerMetaData;
import br.com.cd.mvo.core.WriteableMetaData;

public class ControllerMetaDataFactory extends AbstractBeanMetaDataFactory<ControllerMetaData<?>, ControllerBean> {

	public ControllerMetaDataFactory() {
		super(Controller.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ControllerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		ControllerMetaData beanConfig = new ControllerMetaData(propertyMap);

		return beanConfig;
	}
}
