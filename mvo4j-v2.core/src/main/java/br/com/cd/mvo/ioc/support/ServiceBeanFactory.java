package br.com.cd.mvo.ioc.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.DefaultCrudService;
import br.com.cd.mvo.core.ListenableCrudService;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.core.ServiceListener;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.ServiceMetaDataFactory;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.util.GenericsUtils;

public class ServiceBeanFactory extends
		AbstractBeanFactory<ServiceMetaData, ServiceBean> {

	public ServiceBeanFactory(Container container) {
		super(container, new ServiceMetaDataFactory());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<ServiceMetaData> metaDataWrapper)
			throws ConfigurationException {

		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(
				RepositoryMetaData.class, metaDataWrapper.getBeanMetaData()
						.targetEntity());

		BeanMetaDataWrapper repositoryMetaData = container.getBean(beanName,
				BeanMetaDataWrapper.class);

		Repository repository = container.getBean(repositoryMetaData
				.getBeanMetaData().name(), Repository.class);

		DefaultCrudService service = new DefaultCrudService<>(repository,
				metaDataWrapper.getBeanMetaData());

		return service;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BeanObject wrap(BeanObject bean) {

		if (!(bean instanceof ListenableCrudService))
			return bean;

		ListenableCrudService service = (ListenableCrudService) bean;

		Collection<ServiceListener> listeners = new ArrayList<>();
		try {
			listeners = container.getBeansOfType(ServiceListener.class);
		} catch (NoSuchBeanDefinitionException e) {
		}

		if (ServiceListener.class.isAssignableFrom(service.getClass())) {
			service.addListener((ServiceListener) service);
		}

		for (ServiceListener listener : listeners) {
			Class<?> targetEntity = GenericsUtils.getTypesFor(
					listener.getClass(), ServiceListener.class).get(0);

			if (service.getBeanMetaData().targetEntity().equals(targetEntity)
					&& !listener.getClass().equals(service.getClass())) {

				service.addListener(listener);
			}
		}
		service.afterPropertiesSet();

		return service;
	}
}