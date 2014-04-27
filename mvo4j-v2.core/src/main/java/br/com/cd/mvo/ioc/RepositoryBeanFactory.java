package br.com.cd.mvo.ioc;

import br.com.cd.mvo.RepositoryBean;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.ioc.scan.RepositoryMetaDataFactory;
import br.com.cd.mvo.orm.AbstractRepositoryFactory;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.orm.RepositoryListenerMethodInterceptor;
import br.com.cd.util.StringUtils;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AbstractMethodInvokeCallback proxify(BeanObject<?> bean, RepositoryMetaData<?> metaData) throws ConfigurationException {

		/*
		 * Class<RepositoryFactory> repositoryFactoryClass; try {
		 * repositoryFactoryClass = (Class<RepositoryFactory>)
		 * metaData.persistenceProvider(); } catch (ClassCastException e) {
		 * throw new ConfigurationException(e); }
		 * 
		 * String providerBeanName =
		 * AbstractRepositoryFactory.getBeanName(metaData
		 * .persistenceManagerQualifier());
		 * 
		 * looger.debug(StringUtils.format(
		 * "lookup for repository factory from beanName '{0}', type '{1}' ",
		 * providerBeanName, repositoryFactoryClass)); RepositoryFactory pmf =
		 * container.getPersistenceManagerFactory(providerBeanName,
		 * repositoryFactoryClass);
		 * 
		 * Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME,
		 * Proxifier.class);
		 * 
		 * Repository<?, ?> repository = (Repository<?, ?>) bean;
		 * 
		 * MethodInvokeCallback miCallback = new
		 * RepositoryListenerMethodInterceptor(repository, container);
		 * 
		 * String beanName =
		 * org.apache.commons.lang3.StringUtils.capitalize(metaData.name());
		 * 
		 * Constructor<?> constructor; try { constructor =
		 * repository.getClass().
		 * getConstructor(pmf.getPersistenceManagagerType(),
		 * RepositoryMetaData.class); } catch (NoSuchMethodException |
		 * SecurityException e) { throw new ConfigurationException(e); }
		 * 
		 * Class<? extends Repository> proxyClass = proxifier.proxify(beanName,
		 * repository.getClass(), repository, miCallback);
		 * 
		 * return container.getBean(proxyClass);
		 */
		return new RepositoryListenerMethodInterceptor((Repository<?, ?>) bean, container);
	}
}