package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.orm.PersistenceManagerFactory;

@SuppressWarnings("rawtypes")
public class RepositoryBeanFactory extends
		AbstractBeanFactory<RepositoryMetaData, RepositoryBean> {

	public RepositoryBeanFactory(Container container) {
		super(container);
	}

	@Override
	protected Class<? extends BeanObject> getBeanType(
			RepositoryMetaData metaData) {

		throw new UnsupportedOperationException("@TODO: message");
	}

	@Override
	public Class<BeanObject> createProxy(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws NoSuchBeanDefinitionException {

		throw new UnsupportedOperationException("@TODO: message");
	}

	@SuppressWarnings("unchecked")
	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper)
			throws ConfigurationException {

		String persistenceImplClassName = container.getInitApplicationConfig()
				.getPersistenceManagerFactoryClass();

		if (!metaDataWrapper.getBeanMetaData().persistenceProvider().isEmpty()) {
			persistenceImplClassName = metaDataWrapper.getBeanMetaData()
					.persistenceProvider();
		}

		String beanName = PersistenceManagerFactory.getBeanName(metaDataWrapper
				.getBeanMetaData().persistenceManagerQualifier());

		Class<? extends PersistenceManagerFactory> persistenceProviderClass = loadClass(persistenceImplClassName);
		PersistenceManagerFactory persistenceManagerFactory = container
				.getPersistenceManagerFactory(beanName,
						persistenceProviderClass);

		return persistenceManagerFactory.getRepositoryInstance(metaDataWrapper
				.getBeanMetaData().persistenceManagerQualifier(),
				metaDataWrapper.getBeanMetaData().targetEntity());
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends PersistenceManagerFactory> loadClass(
			String className) throws ConfigurationException {
		try {
			return (Class<? extends PersistenceManagerFactory>) Class
					.forName(className);
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new ConfigurationException(e);
		}
	}
}