package br.com.cd.mvo.ioc.scan;

import javassist.NotFoundException;
import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.util.ProxyUtils;

public class ServiceMetaDataFactory extends
		AbstractBeanMetaDataFactory<ServiceMetaData, ServiceBean> {

	public ServiceMetaDataFactory() {
		super(CrudService.class);
	}

	@Override
	public ServiceMetaData createBeanMetaData(WriteablePropertyMap propertyMap) {

		ServiceMetaData beanConfig = new ServiceMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public ServiceMetaData createBeanMetaData(Class<?> beanType,
			ServiceBean annotation) throws ConfigurationException {

		WriteablePropertyMap map = new WriteablePropertyMap();

		try {
			map.addAll(ProxyUtils.getAnnotationAtributes(beanType, annotation));
		} catch (NotFoundException e) {
			throw new ConfigurationException(e);
		}

		return createBeanMetaData(map);
	}

	@Override
	public int getOrder() {
		return 2;
	}

}
