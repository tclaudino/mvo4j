package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.ioc.Container;

public class ControllerBeanFactory extends
		AbstractBeanFactory<ControllerMetaData, ControllerBean> {

	public ControllerBeanFactory(Container container) {
		super(container);
	}

	@Override
	protected Class<? extends BeanObject> getBeanType(
			ControllerMetaData beanConfig) {

		return DefaultCrudController.class;
	}
}
