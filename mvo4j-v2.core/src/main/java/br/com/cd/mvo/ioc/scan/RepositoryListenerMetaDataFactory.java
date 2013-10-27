package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ControllerListenerMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.CrudController;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public class RepositoryListenerMetaDataFactory extends
		AbstractBeanMetaDataFactory<ControllerListenerMetaData, NoScan> {

	public RepositoryListenerMetaDataFactory() {
		super(CrudController.class);
	}

	@Override
	public ControllerListenerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE,
				ConfigParamKeys.DefaultValues.SCOPE_SESSION_NAME);

		ControllerListenerMetaData beanConfig = new ControllerListenerMetaData(
				propertyMap);

		return beanConfig;
	}

	@Override
	public BeanMetaDataWrapper<ControllerListenerMetaData> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container, boolean readAnnotationAttributes)
			throws ConfigurationException {

		if (ControllerListener.class.isAssignableFrom(beanType)) {

			Class<?> targetEntity = GenericsUtils.getTypesFor(beanType,
					ControllerListener.class).get(0);
			propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
		}

		return super
				.createBeanMetaData(propertyMap, beanType, container, false);
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		@SuppressWarnings("rawtypes")
		Collection<Class<? extends ControllerListener>> subTypes = scanner
				.scanSubTypesOf(ControllerListener.class, packagesToScan);

		Collection<Class<?>> result = new HashSet<>();
		result.addAll(subTypes);

		return result;
	}

}
