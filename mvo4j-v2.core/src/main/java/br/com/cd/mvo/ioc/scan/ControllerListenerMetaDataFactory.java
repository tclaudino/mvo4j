package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.bean.config.WriteableMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.util.GenericsUtils;

@SuppressWarnings("rawtypes")
public class ControllerListenerMetaDataFactory extends AbstractBeanMetaDataFactory<ControllerMetaData.ListenerMetaData<?>, NoScan> {

	public ControllerListenerMetaDataFactory() {
		super(ControllerListener.class);
	}

	@Override
	public ControllerMetaData.ListenerMetaData doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, ConfigParamKeys.DefaultValues.SCOPE_SESSION_NAME);

		ControllerMetaData.ListenerMetaData beanConfig = new ControllerMetaData.ListenerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<ControllerMetaData.ListenerMetaData<?>> createBeanMetaData(WriteableMetaData propertyMap, Class<?> beanType,
			boolean readAnnotationAttributes) throws ConfigurationException {

		if (ControllerListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypeFor(beanType, ControllerListener.class);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super.createBeanMetaData(propertyMap, beanType, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		Collection<Class<? extends ControllerListener>> subTypes = scanner.scanSubTypesOf(ControllerListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
