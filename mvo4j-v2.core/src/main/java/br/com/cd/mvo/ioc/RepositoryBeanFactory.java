package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.RepositoryMetaDataFactory;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.orm.support.AbstractRepositoryFactory;
import br.com.cd.mvo.util.StringUtils;

public class RepositoryBeanFactory extends AbstractBeanFactory<RepositoryMetaData<?>, RepositoryBean> {

	public RepositoryBeanFactory(Container container) {
		super(container, new RepositoryMetaDataFactory());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BeanObject<?> getInstance(RepositoryMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating wrapped repository bean from metaData '{0}', persistenceProviderType '{1}'...'",
				metaData, metaData.persistenceProvider()));

		Class<RepositoryFactory> repositoryFactoryClass;
		try {
			repositoryFactoryClass = (Class<RepositoryFactory>) metaData.persistenceProvider();
		} catch (ClassCastException e) {
			throw new ConfigurationException(e);
		}

		String providerBeanName = AbstractRepositoryFactory.getBeanName(metaData.persistenceManagerQualifier());

		looger.debug(StringUtils.format("lookup for repository factory from beanName '{0}', type '{1}' ", providerBeanName,
				repositoryFactoryClass));
		RepositoryFactory pmf = container.getPersistenceManagerFactory(providerBeanName, repositoryFactoryClass);

		looger.debug("getting wrapped instance...");
		Repository repository = pmf.getInstance(metaData);

		looger.debug(StringUtils.format("returning repository bean instance '{0}'", repository));
		looger.debug("...............................................................................");
		return repository;
	}

	/*
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 *
	 * @Override public <T> BeanObject<T> wrap(BeanObject<T> bean) {
	 *
	 * if (!(bean instanceof ListenableRepository)) return bean;
	 *
	 * ListenableRepository<T> repository = (ListenableRepository<T>) bean;
	 *
	 * if (RepositoryListener.class.isAssignableFrom(repository.getClass())) {
	 *
	 * repository.setListener((RepositoryListener<T>) repository); } else {
	 *
	 * Collection<RepositoryListener> listeners = new ArrayList<>(); try {
	 * listeners = container.getBeansOfType(RepositoryListener.class); } catch
	 * (NoSuchBeanDefinitionException e) { // TODO LOG this }
	 *
	 * for (RepositoryListener listener : listeners) { Class<?> targetEntity =
	 * GenericsUtils.getTypesFor(listener.getClass(),
	 * RepositoryListener.class).get(0);
	 *
	 * if (repository.getBeanMetaData().targetEntity().equals(targetEntity)) {
	 *
	 * repository.setListener(listener); break; } } }
	 * repository.afterPropertiesSet();
	 *
	 * return repository; }
	 */

}