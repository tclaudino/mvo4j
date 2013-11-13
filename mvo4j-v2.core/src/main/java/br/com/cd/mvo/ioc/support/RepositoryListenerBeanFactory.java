package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.NoScan;
import br.com.cd.mvo.ioc.scan.RepositoryListenerMetaDataFactory;
import br.com.cd.mvo.util.StringUtils;

public class RepositoryListenerBeanFactory extends AbstractBeanFactory<RepositoryMetaData.ListenerMetaData<?>, NoScan> {

	public RepositoryListenerBeanFactory(Container container) {
		super(container, new RepositoryListenerMetaDataFactory());
	}

	@Override
	public BeanObject<?> getInstance(RepositoryMetaData.ListenerMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating repository listener bean from metaData '{0}'...", metaData));

		String beanName = metaData.name().replace(metaData.getBeanNameSuffix(), "");
		looger.debug(StringUtils.format("lookup from repository bean from this listener beanName '{0}'", beanName));
		BeanObject<?> bean = container.getBean(beanName, BeanObject.class);

		looger.debug(StringUtils.format("lookup from beanName '{0}'", metaData.name()));

		// TODO: BeanObject<?> bean = container.getBean(metaData.name(),
		// BeanObject.class);

		looger.debug(StringUtils.format("found bean '{0}'. returning...", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	@Override
	public void postConstruct(BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper) {

		// only to prevent super;
	}
}