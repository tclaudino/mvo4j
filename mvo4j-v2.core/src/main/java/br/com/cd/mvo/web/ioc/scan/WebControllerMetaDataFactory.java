package br.com.cd.mvo.web.ioc.scan;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.ioc.scan.AbstractBeanMetaDataFactory;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class WebControllerMetaDataFactory extends
		AbstractBeanMetaDataFactory<WebControllerMetaData, WebControllerBean> {

	public WebControllerMetaDataFactory() {
		super(WebCrudController.class);
	}

	@Override
	public WebControllerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "session");
		WebControllerMetaData beanConfig = new WebControllerMetaData(
				propertyMap);

		return beanConfig;
	}

	@Override
	public int getOrder() {
		return 4;
	}
}
