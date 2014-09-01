package br.com.cd.mvo.web.ioc;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.ServiceMetaData;
import br.com.cd.mvo.ioc.AbstractBeanFactory;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.orm.ControllerListenerMethodInterceptor;
import br.com.cd.mvo.web.WebControllerBean;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.core.DefaultWebCrudController;
import br.com.cd.mvo.web.core.WebControllerMetaData;
import br.com.cd.mvo.web.ioc.scan.WebControllerMetaDataFactory;
import br.com.cd.util.StringUtils;

public class WebControllerBeanFactory extends AbstractBeanFactory<WebControllerMetaData<?>, WebControllerBean> {

	public WebControllerBeanFactory(Container container) {
		super(container, new WebControllerMetaDataFactory());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BeanObject<?> getInstance(WebControllerMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating wrapped webController bean from metaData '{0}'...'", metaData));

		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(ServiceMetaData.class, metaData.targetEntity());

		BeanMetaDataWrapper serviceMetaData = container.getBean(beanName, BeanMetaDataWrapper.class);

		looger.debug(StringUtils.format("lookup for service bean from beanName '{0}'", serviceMetaData.getBeanMetaData().name()));
		CrudService service = container.getBean(serviceMetaData.getBeanMetaData().name(), CrudService.class);
		looger.debug(StringUtils.format("found service bean '{0}'", service));

		Application application = container.getBean(Application.BEAN_NAME, Application.class);
		Translator translator = container.getBean(Translator.BEAN_NAME, Translator.class);
		DataModelFactory dataModelFactory = container.getBean(DataModelFactory.BEAN_NAME, DataModelFactory.class);

		@SuppressWarnings("unchecked")
		DefaultWebCrudController bean = new DefaultWebCrudController(application, translator, dataModelFactory, service, metaData);

		looger.debug(StringUtils.format("returning webController bean instance '{0}'", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	@Override
	public MethodInvokeCallback proxify(BeanObject<?> bean, WebControllerMetaData<?> metaData) throws ConfigurationException {

		/*
		 * Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME,
		 * Proxifier.class);
		 * 
		 * WebCrudController<?> controller = (WebCrudController<?>) bean;
		 * 
		 * @SuppressWarnings({ "unchecked", "rawtypes" }) MethodInvokeCallback
		 * miCallback = new ControllerListenerMethodInterceptor(controller,
		 * container);
		 * 
		 * String beanName =
		 * org.apache.commons.lang3.StringUtils.capitalize(metaData.name());
		 * 
		 * Constructor<?> constructor; try { constructor =
		 * bean.getClass().getConstructor(Application.class, Translator.class,
		 * DataModelFactory.class, CrudService.class,
		 * WebControllerMetaData.class); } catch (NoSuchMethodException |
		 * SecurityException e) { throw new ConfigurationException(e); }
		 * 
		 * DataModelFactory dataModelFactory =
		 * container.getBean(DataModelFactory.BEAN_NAME,
		 * DataModelFactory.class);
		 * 
		 * return proxifier.proxify(beanName, controller.getClass(), controller,
		 * constructor, miCallback, controller.getApplication(),
		 * controller.getTranslator(), dataModelFactory,
		 * controller.getService(), metaData);
		 */
		return new ControllerListenerMethodInterceptor((WebCrudController<?>) bean, container);

	}
}