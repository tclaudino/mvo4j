package br.com.cd.mvo.web.ioc.scan;

import javassist.NotFoundException;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.scan.AbstractBeanMetaDataFactory;
import br.com.cd.mvo.util.ProxyUtils;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class WebControllerMetaDataFactory extends
		AbstractBeanMetaDataFactory<WebControllerMetaData, WebControllerBean> {

	public WebControllerMetaDataFactory() {
		super(WebCrudController.class);
	}

	@Override
	public WebControllerMetaData createBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "session");
		WebControllerMetaData beanConfig = new WebControllerMetaData(
				propertyMap);

		return beanConfig;
	}

	@Override
	public WebControllerMetaData createBeanMetaData(Class<?> beanType,
			WebControllerBean annotation) throws ConfigurationException {

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
		return 4;
	}

}
