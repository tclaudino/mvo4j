package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.ControllerListenerMetaDataFactory;
import br.com.cd.mvo.ioc.scan.NoScan;
import br.com.cd.mvo.util.StringUtils;

public class ControllerListenerBeanFactory extends AbstractBeanFactory<ControllerMetaData.ListenerMetaData<?>, NoScan> {

	public ControllerListenerBeanFactory(Container container) {
		super(container, new ControllerListenerMetaDataFactory());
	}

	@Override
	public BeanObject<?> getInstance(ControllerMetaData.ListenerMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating controller listener bean from metaData '{0}'...", metaData));
		looger.debug(StringUtils.format("lookup from beanName '{0}'", metaData.name()));

		BeanObject<?> bean = container.getBean(metaData.name(), BeanObject.class);

		looger.debug(StringUtils.format("found bean '{0}'. returning...", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	@Override
	public void postConstruct(BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper) {

		// only to prevent super;
	}

}
