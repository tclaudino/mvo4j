package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.NoScan;
import br.com.cd.mvo.ioc.scan.ServiceListenerMetaDataFactory;
import br.com.cd.mvo.util.StringUtils;

public class ServiceListenerBeanFactory extends AbstractBeanFactory<ServiceMetaData.ListenerMetaData<?>, NoScan> {

	public ServiceListenerBeanFactory(Container container) {
		super(container, new ServiceListenerMetaDataFactory());
	}

	@Override
	public BeanObject<?> getInstance(ServiceMetaData.ListenerMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating service listener bean from metaData '{0}'...", metaData));
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