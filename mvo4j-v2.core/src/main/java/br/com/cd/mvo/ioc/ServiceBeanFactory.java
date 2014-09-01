package br.com.cd.mvo.ioc;

import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.ServiceBean;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultCrudService;
import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.core.ServiceMetaData;
import br.com.cd.mvo.ioc.scan.ServiceMetaDataFactory;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.ServiceListenerMethodInterceptor;
import br.com.cd.util.StringUtils;

public class ServiceBeanFactory extends AbstractBeanFactory<ServiceMetaData<?>, ServiceBean> {

	public ServiceBeanFactory(Container container) {
		super(container, new ServiceMetaDataFactory());
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public BeanObject<?> getInstance(ServiceMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating wrapped service bean from metaData '{0}'...'", metaData));
		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(RepositoryMetaData.class, metaData.targetEntity());

		BeanMetaDataWrapper repositoryMetaData = container.getBean(beanName, BeanMetaDataWrapper.class);

		looger.debug(StringUtils.format("lookup for repository bean from beanName '{0}'", repositoryMetaData.getBeanMetaData().name()));
		Repository repository = container.getBean(repositoryMetaData.getBeanMetaData().name(), Repository.class);
		looger.debug(StringUtils.format("found repository bean '{0}'", repository));

		@SuppressWarnings("unchecked")
		DefaultCrudService bean = new DefaultCrudService(repository, metaData);

		looger.debug(StringUtils.format("returning service bean instance '{0}'", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public MethodInvokeCallback proxify(BeanObject<?> bean, ServiceMetaData<?> metaData) throws ConfigurationException {

		/*
		 * Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME,
		 * Proxifier.class);
		 * 
		 * CrudService<?> service = (CrudService<?>) bean;
		 * 
		 * MethodInvokeCallback miCallback = new
		 * ServiceListenerMethodInterceptor(service, container);
		 * 
		 * String beanName =
		 * org.apache.commons.lang3.StringUtils.capitalize(metaData.name());
		 * 
		 * Constructor<?> constructor; try { constructor =
		 * bean.getClass().getConstructor(Repository.class,
		 * ServiceMetaData.class); } catch (NoSuchMethodException |
		 * SecurityException e) { throw new ConfigurationException(e); }
		 * 
		 * return proxifier.proxify(beanName, service.getClass(), service,
		 * constructor, miCallback, service.getRepository(), metaData);
		 */
		return new ServiceListenerMethodInterceptor((CrudService<?>) bean, container);
	}
}