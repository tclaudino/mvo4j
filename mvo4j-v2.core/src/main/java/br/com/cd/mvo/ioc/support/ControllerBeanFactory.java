package br.com.cd.mvo.ioc.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.core.ListenableController;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.ControllerMetaDataFactory;
import br.com.cd.mvo.util.GenericsUtils;

public class ControllerBeanFactory extends
		AbstractBeanFactory<ControllerMetaData, ControllerBean> {

	public ControllerBeanFactory(Container container) {
		super(container, new ControllerMetaDataFactory());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<ControllerMetaData> metaDataWrapper)
			throws ConfigurationException {

		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(
				ServiceMetaData.class, metaDataWrapper.getBeanMetaData()
						.targetEntity());

		BeanMetaDataWrapper serviceMetaData = container.getBean(beanName,
				BeanMetaDataWrapper.class);

		CrudService service = container.getBean(serviceMetaData
				.getBeanMetaData().name(), CrudService.class);

		DefaultCrudController bean = new DefaultCrudController(
				container.getBean(Application.BEAN_NAME, Application.class),
				container.getBean(Translator.BEAN_NAME, Translator.class),
				container.getBean(DataModelFactory.BEAN_NAME,
						DataModelFactory.class), service,
				metaDataWrapper.getBeanMetaData());

		return bean;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BeanObject wrap(BeanObject bean) {

		if (!(bean instanceof ListenableController))
			return bean;

		ListenableController controller = (ListenableController) bean;

		Collection<ControllerListener> listeners = new ArrayList<>();
		try {
			listeners = container.getBeansOfType(ControllerListener.class);
		} catch (NoSuchBeanDefinitionException e) {
		}

		if (ControllerListener.class.isAssignableFrom(controller.getClass())) {
			controller.addListener((ControllerListener) controller);
		}

		for (ControllerListener listener : listeners) {
			Class<?> targetEntity = GenericsUtils.getTypesFor(
					listener.getClass(), ControllerListener.class).get(0);

			if (controller.getBeanMetaData().targetEntity()
					.equals(targetEntity)
					&& !listener.getClass().equals(controller.getClass())) {

				controller.addListener(listener);
			}
		}
		controller.afterPropertiesSet();

		return controller;
	}
}
