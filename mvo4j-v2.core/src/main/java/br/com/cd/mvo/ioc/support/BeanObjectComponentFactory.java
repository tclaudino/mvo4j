package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.config.ControllerListenerMetaData;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.GenericsUtils;

public class BeanObjectComponentFactory<O extends BeanObject> extends
		AbstractComponentFactory<O> {

	private Class<O> objectType;
	private String beanName;

	public BeanObjectComponentFactory(Container container, Class<O> objectType,
			String beanName) {
		super(container);
		this.objectType = objectType;
		this.beanName = beanName;
	}

	@Override
	public Class<O> getObjectType() {
		return objectType;
	}

	@Override
	public O getInstance() throws NoSuchBeanDefinitionException {

		return this.getInstanceInternal();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected O getInstanceInternal() throws NoSuchBeanDefinitionException {

		O bean = container.getBean(beanName, objectType);

		if (!Controller.class.isAssignableFrom(objectType)) {
			return bean;
		}

		Controller<?> controller = (Controller<?>) bean;

		if (!container.containsBean(beanName
				+ ControllerListenerMetaData.BEAN_NAME_SUFFIX)) {
			return bean;
		}

		ControllerListener listener;
		try {
			listener = container.getBean(beanName
					+ ControllerListenerMetaData.BEAN_NAME_SUFFIX,
					ControllerListener.class);
		} catch (NoSuchBeanDefinitionException e) {
			throw new NoSuchBeanDefinitionException(objectType.getName()
					+ " does not is a instance of"
					+ ControllerListener.class.getName());
		}

		Class<?> targetEntity = GenericsUtils.getTypesFor(listener.getClass(),
				ControllerListener.class).get(0);

		if (!controller.getControllerConfig().targetEntity()
				.equals(targetEntity)) {
			throw new NoSuchBeanDefinitionException(controller
					.getControllerConfig().targetEntity()
					+ " does not is a instance of" + targetEntity);
		}

		controller.addListener(listener);

		return bean;

	}
}