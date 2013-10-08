package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultCrudService;
import br.com.cd.mvo.ioc.Container;

public class ServiceBeanFactory extends
		AbstractBeanFactory<ServiceMetaData, ServiceBean> {

	public ServiceBeanFactory(Container container) {
		super(container);
	}

	@Override
	protected Class<? extends BeanObject> getBeanType(ServiceMetaData metaData) {

		return DefaultCrudService.class;
	}
}