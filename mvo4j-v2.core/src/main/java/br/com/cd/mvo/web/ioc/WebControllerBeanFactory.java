package br.com.cd.mvo.web.ioc;

import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.AbstractBeanFactory;
import br.com.cd.mvo.web.DefaultWebCrudController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class WebControllerBeanFactory extends
		AbstractBeanFactory<WebControllerMetaData, WebControllerBean> {

	public WebControllerBeanFactory(Container container) {
		super(container);
	}

	@Override
	protected Class<? extends BeanObject> getBeanType(
			WebControllerMetaData beanConfig) {

		return DefaultWebCrudController.class;
	}
}