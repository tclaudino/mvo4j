package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.RepositoryListener;
import br.com.cd.mvo.util.GenericsUtils;

public class RepositoryListenerMetaDataFactory
		extends
		AbstractBeanMetaDataFactory<RepositoryMetaData.ListenerMetaData, NoScan> {

	public RepositoryListenerMetaDataFactory() {
		super(Repository.class);
	}

	@Override
	public RepositoryMetaData.ListenerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE,
				ConfigParamKeys.DefaultValues.SCOPE_SESSION_NAME);

		RepositoryMetaData.ListenerMetaData beanConfig = new RepositoryMetaData.ListenerMetaData(
				propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<RepositoryMetaData.ListenerMetaData> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container, boolean readAnnotationAttributes)
			throws ConfigurationException {

		if (RepositoryListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypesFor(beanType,
					RepositoryListener.class).get(0);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super
				.createBeanMetaData(propertyMap, beanType, container, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		@SuppressWarnings("rawtypes")
		Collection<Class<? extends RepositoryListener>> subTypes = scanner
				.scanSubTypesOf(RepositoryListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
