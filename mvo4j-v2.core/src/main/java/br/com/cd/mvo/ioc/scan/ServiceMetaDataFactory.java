package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.core.CrudService;

public class ServiceMetaDataFactory extends
		AbstractBeanMetaDataFactory<ServiceMetaData, ServiceBean> {

	public ServiceMetaDataFactory() {
		super(CrudService.class);
	}

	@Override
	public ServiceMetaData doCreateBeanMetaData(WriteablePropertyMap propertyMap) {

		ServiceMetaData beanConfig = new ServiceMetaData(propertyMap);

		return beanConfig;
	}
}
