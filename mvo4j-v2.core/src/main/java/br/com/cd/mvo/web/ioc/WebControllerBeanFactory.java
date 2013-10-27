package br.com.cd.mvo.web.ioc;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.ListenableController;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.AbstractBeanFactory;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.web.DefaultWebCrudController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;
import br.com.cd.mvo.web.ioc.scan.WebControllerMetaDataFactory;

public class WebControllerBeanFactory extends
		AbstractBeanFactory<WebControllerMetaData, WebControllerBean> {

	public WebControllerBeanFactory(Container container) {
		super(container, new WebControllerMetaDataFactory());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BeanObject getInstance(
			BeanMetaDataWrapper<WebControllerMetaData> metaDataWrapper)
			throws ConfigurationException {

		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(
				ServiceMetaData.class, metaDataWrapper.getBeanMetaData()
						.targetEntity());

		BeanMetaDataWrapper serviceMetaData = container.getBean(beanName,
				BeanMetaDataWrapper.class);

		CrudService service = container.getBean(serviceMetaData
				.getBeanMetaData().name(), CrudService.class);

		DefaultWebCrudController bean = new DefaultWebCrudController(
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