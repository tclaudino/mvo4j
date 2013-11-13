package br.com.cd.mvo.web.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.ioc.scan.AbstractBeanMetaDataFactory;
import br.com.cd.mvo.web.WebController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;

public class WebControllerMetaDataFactory extends AbstractBeanMetaDataFactory<WebControllerMetaData<?>, WebControllerBean> {

	public WebControllerMetaDataFactory() {
		super(WebController.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public WebControllerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, ConfigParamKeys.DefaultValues.SCOPE_SESSION_NAME);
		WebControllerMetaData beanConfig = new WebControllerMetaData(propertyMap);

		return beanConfig;
	}
}
