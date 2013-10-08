package br.com.cd.mvo.ioc.scan;

import javassist.NotFoundException;
import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudController;
import br.com.cd.mvo.util.ProxyUtils;

public class ControllerMetaDataFactory extends
		AbstractBeanMetaDataFactory<ControllerMetaData, ControllerBean> {

	public ControllerMetaDataFactory() {
		super(CrudController.class);
	}

	@Override
	public ControllerMetaData createBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "session");

		ControllerMetaData beanConfig = new ControllerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public ControllerMetaData createBeanMetaData(Class<?> beanType,
			ControllerBean annotation) throws ConfigurationException {

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
		return 3;
	}

}
