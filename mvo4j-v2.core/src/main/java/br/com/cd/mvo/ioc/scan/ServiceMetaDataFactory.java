package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.core.CrudService;

public class ServiceMetaDataFactory extends AbstractBeanMetaDataFactory<ServiceMetaData<?>, ServiceBean> {

	public ServiceMetaDataFactory() {
		super(CrudService.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ServiceMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, ConfigParamKeys.DefaultValues.SCOPE_SINGLETON_NAME);

		ServiceMetaData beanConfig = new ServiceMetaData(propertyMap);

		return beanConfig;
	}
}
