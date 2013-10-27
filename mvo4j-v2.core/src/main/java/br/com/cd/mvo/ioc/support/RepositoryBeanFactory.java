package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.RepositoryMetaDataFactory;
import br.com.cd.mvo.orm.ListenableRepository;
import br.com.cd.mvo.orm.RepositoryFactory;
import br.com.cd.mvo.orm.RepositoryListener;
import br.com.cd.mvo.orm.support.AbstractRepositoryFactory;

@SuppressWarnings("rawtypes")
public class RepositoryBeanFactory extends
		AbstractBeanFactory<RepositoryMetaData, RepositoryBean> {

	public RepositoryBeanFactory(Container container) {
		super(container, new RepositoryMetaDataFactory());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<RepositoryMetaData> metaDataWrapper)
			throws ConfigurationException {

		Class<RepositoryFactory> repositoryFactoryClass;
		try {
			repositoryFactoryClass = (Class<RepositoryFactory>) metaDataWrapper
					.getBeanMetaData().persistenceProvider();
		} catch (ClassCastException e) {
			throw new ConfigurationException(e);
		}

		String providerBeanName = AbstractRepositoryFactory
				.getBeanName(metaDataWrapper.getBeanMetaData()
						.persistenceManagerQualifier());

		RepositoryFactory pmf = container.getPersistenceManagerFactory(
				providerBeanName, repositoryFactoryClass);

		final ListenableRepository repository = pmf.getInstance(metaDataWrapper
				.getBeanMetaData().persistenceManagerQualifier(),
				metaDataWrapper.getBeanMetaData().targetEntity(),
				metaDataWrapper.getBeanMetaData());

		return repository;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public BeanObject wrap(BeanObject bean) {

		if (!(bean instanceof ListenableRepository))
			return bean;

		ListenableRepository repository = (ListenableRepository) bean;

		if (RepositoryListener.class.isAssignableFrom(repository.getClass())) {

			repository.setListener((RepositoryListener) repository);
		} else {

			try {
				RepositoryListener listener = container
						.getBean(RepositoryListener.class);
				repository.setListener(listener);
			} catch (NoSuchBeanDefinitionException e) {
			}
		}
		repository.afterPropertiesSet();

		return repository;
	}

}