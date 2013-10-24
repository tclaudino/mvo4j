package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.core.CrudController;

public class ControllerMetaDataFactory extends
		AbstractBeanMetaDataFactory<ControllerMetaData, ControllerBean> {

	public ControllerMetaDataFactory() {
		super(CrudController.class);
	}

	@Override
	public ControllerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "session");

		ControllerMetaData beanConfig = new ControllerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public int getOrder() {
		return 3;
	}

}
