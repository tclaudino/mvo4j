package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.DefaultCrudService;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.ServiceMetaDataFactory;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.util.StringUtils;

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

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 *
	 * @Override public <T> BeanObject<T> wrap(BeanObject<T> bean) {
	 *
	 * if (!(bean instanceof CrudServiceListener)) return bean;
	 *
	 * CrudServiceListener<T> service = (CrudServiceListener<T>) bean;
	 *
	 * Collection<BeanObjectListener> listeners = new ArrayList<>(); try {
	 * listeners = container.getBeansOfType(BeanObjectListener.class); } catch
	 * (NoSuchBeanDefinitionException e) { // TODO LOG this }
	 *
	 * if (BeanObjectListener.class.isAssignableFrom(service.getClass())) {
	 * listeners.add((BeanObjectListener) service); }
	 *
	 * for (BeanObjectListener listener : listeners) { Class<?> targetEntity =
	 * GenericsUtils.getTypesFor(listener.getClass(),
	 * BeanObjectListener.class).get(0);
	 *
	 * if (service.getBeanMetaData().targetEntity().equals(targetEntity)) {
	 *
	 * service.addListener(listener); }
	 *
	 * } service.afterPropertiesSet();
	 *
	 * return service; }
	 */
}