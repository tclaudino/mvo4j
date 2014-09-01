package br.com.cd.mvo.web.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.ioc.scan.AbstractBeanMetaDataFactory;
import br.com.cd.mvo.web.WebController;
import br.com.cd.mvo.web.WebControllerBean;
import br.com.cd.mvo.web.core.WebControllerMetaData;

public class WebControllerMetaDataFactory extends AbstractBeanMetaDataFactory<WebControllerMetaData<?>, WebControllerBean> {

	public WebControllerMetaDataFactory() {
		super(WebController.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public WebControllerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE_NAME, ConfigParamKeys.DefaultValues.SCOPE_NAME_SESSION);
		WebControllerMetaData beanConfig = new WebControllerMetaData(propertyMap);

		return beanConfig;
	}
}
