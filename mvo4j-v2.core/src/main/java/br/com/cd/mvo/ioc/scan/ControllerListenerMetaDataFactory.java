package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.TreeSet;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.CrudController;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.BeanFactoryUtils;
import br.com.cd.mvo.util.GenericsUtils;

public class ControllerListenerMetaDataFactory extends
		AbstractBeanMetaDataFactory<ControllerMetaData, ControllerBean> {

	public ControllerListenerMetaDataFactory() {
		super(CrudController.class);
	}

	@Override
	public ControllerMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "session");

		ControllerMetaData beanConfig = new ControllerMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public Collection<Class<?>> scan(Scanner scanner, String[] packagesToScan) {

		@SuppressWarnings("rawtypes")
		Collection<Class<? extends ControllerListener>> subTypes = scanner
				.scanSubTypesOf(ControllerListener.class, packagesToScan);

		Collection<Class<?>> result = new TreeSet<>();
		result.addAll(subTypes);

		return result;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BeanMetaDataWrapper<ControllerMetaData> createBeanMetaData(
			WriteablePropertyMap propertyMap, Class<?> beanType,
			Container container) throws ConfigurationException {

		if (!ControllerListener.class.isAssignableFrom(beanType)) {
			throw new ConfigurationException(new IllegalArgumentException(
					beanType.getName() + " does not is a instance of"
							+ ControllerListener.class.getName()));
		}

		Class<?> targetEntity = GenericsUtils.getTypesFor(beanType).get(1);

		propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);

		BeanMetaDataWrapper<ControllerMetaData> metaDataWrapper = this
				.createBeanMetaData(propertyMap);

		String beanName = BeanFactoryUtils.generateBeanName(metaDataWrapper);

		String listenerName = beanName + "Listener";
		container.registerBean(beanType, listenerName);

		ControllerListener listener = (ControllerListener) container.getBean(
				listenerName, beanType);

		CrudController bean = container.getBean(beanName, CrudController.class);

		bean.addListener(listener);

		return metaDataWrapper;
	}

	@Override
	public int getOrder() {
		return 5;
	}

}
