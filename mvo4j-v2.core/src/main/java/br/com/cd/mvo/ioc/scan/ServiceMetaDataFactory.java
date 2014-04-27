package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.ServiceBean;
import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.ServiceMetaData;
import br.com.cd.mvo.core.WriteableMetaData;

public class ServiceMetaDataFactory extends AbstractBeanMetaDataFactory<ServiceMetaData<?>, ServiceBean> {

	public ServiceMetaDataFactory() {
		super(CrudService.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ServiceMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE_NAME, ConfigParamKeys.DefaultValues.SCOPE_NAME_SINGLETON);

		ServiceMetaData beanConfig = new ServiceMetaData(propertyMap);

		return beanConfig;
	}
}
