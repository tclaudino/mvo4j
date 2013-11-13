package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.orm.RepositoryListener;
import br.com.cd.mvo.util.GenericsUtils;

@SuppressWarnings("rawtypes")
public class RepositoryListenerMetaDataFactory extends AbstractBeanMetaDataFactory<RepositoryMetaData.ListenerMetaData<?>, NoScan> {

	public RepositoryListenerMetaDataFactory() {
		super(RepositoryListener.class);
	}

	@Override
	public RepositoryMetaData.ListenerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, ConfigParamKeys.DefaultValues.SCOPE_SINGLETON_NAME);

		RepositoryMetaData.ListenerMetaData beanConfig = new RepositoryMetaData.ListenerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<RepositoryMetaData.ListenerMetaData<?>> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType,
			boolean readAnnotationAttributes) throws ConfigurationException {

		if (RepositoryListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypeFor(beanType, RepositoryListener.class);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super.createBeanMetaData(propertyMap, beanType, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		Collection<Class<? extends RepositoryListener>> subTypes = scanner.scanSubTypesOf(RepositoryListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
