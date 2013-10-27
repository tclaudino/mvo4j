package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.ServiceListener;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public class ServiceListenerMetaDataFactory extends
		AbstractBeanMetaDataFactory<ServiceMetaData.ListenerMetaData, NoScan> {

	public ServiceListenerMetaDataFactory() {
		super(CrudService.class);
	}

	@Override
	public ServiceMetaData.ListenerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE,
				ConfigParamKeys.DefaultValues.SCOPE_SESSION_NAME);

		ServiceMetaData.ListenerMetaData beanConfig = new ServiceMetaData.ListenerMetaData(
				propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<ServiceMetaData.ListenerMetaData> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container, boolean readAnnotationAttributes)
			throws ConfigurationException {

		if (ServiceListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypesFor(beanType,
					ServiceListener.class).get(0);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super
				.createBeanMetaData(propertyMap, beanType, container, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		@SuppressWarnings("rawtypes")
		Collection<Class<? extends ServiceListener>> subTypes = scanner
				.scanSubTypesOf(ServiceListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
