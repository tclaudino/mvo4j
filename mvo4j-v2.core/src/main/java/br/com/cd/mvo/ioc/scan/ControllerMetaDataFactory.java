package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.core.Controller;

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
