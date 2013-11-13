package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObjectListener;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudServiceListener;
import br.com.cd.mvo.util.GenericsUtils;

@SuppressWarnings("rawtypes")
public class ServiceListenerMetaDataFactory extends AbstractBeanMetaDataFactory<ServiceMetaData.ListenerMetaData<?>, NoScan> {

	public ServiceListenerMetaDataFactory() {
		super(CrudServiceListener.class);
	}

	@Override
	public ServiceMetaData.ListenerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, ConfigParamKeys.DefaultValues.SCOPE_SINGLETON_NAME);

		ServiceMetaData.ListenerMetaData beanConfig = new ServiceMetaData.ListenerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<ServiceMetaData.ListenerMetaData<?>> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType,
			boolean readAnnotationAttributes) throws ConfigurationException {

		if (BeanObjectListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypeFor(beanType, BeanObjectListener.class);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super.createBeanMetaData(propertyMap, beanType, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		Collection<Class<? extends BeanObjectListener>> subTypes = scanner.scanSubTypesOf(BeanObjectListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
