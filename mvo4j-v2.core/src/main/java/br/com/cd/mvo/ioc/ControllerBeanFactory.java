package br.com.cd.mvo.ioc;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.ControllerBean;
import br.com.cd.mvo.CrudController;
import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ControllerMetaData;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.core.ServiceMetaData;
import br.com.cd.mvo.ioc.scan.ControllerMetaDataFactory;
import br.com.cd.mvo.orm.ControllerListenerMethodInterceptor;
import br.com.cd.util.StringUtils;

public class ControllerBeanFactory extends AbstractBeanFactory<ControllerMetaData<?>, ControllerBean> {

	public ControllerBeanFactory(Container container) {
		super(container, new ControllerMetaDataFactory());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BeanObject<?> getInstance(ControllerMetaData<?> metaData) throws ConfigurationException {

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("creating wrapped controller bean from metaData '{0}'...'", metaData));

		String beanName = BeanMetaDataWrapper.generateBeanMetaDataName(ServiceMetaData.class, metaData.targetEntity());

		BeanMetaDataWrapper serviceMetaData = container.getBean(beanName, BeanMetaDataWrapper.class);

		looger.debug(StringUtils.format("lookup for service bean from beanName '{0}'", serviceMetaData.getBeanMetaData().name()));
		CrudService service = container.getBean(serviceMetaData.getBeanMetaData().name(), CrudService.class);
		looger.debug(StringUtils.format("found service bean '{0}'", service));

		Application application = container.getBean(Application.BEAN_NAME, Application.class);
		Translator translator = container.getBean(Translator.BEAN_NAME, Translator.class);
		DataModelFactory dataModelFactory = container.getBean(DataModelFactory.BEAN_NAME, DataModelFactory.class);
		@SuppressWarnings({ "unchecked" })
		DefaultCrudController bean = new DefaultCrudController(application, translator, dataModelFactory, service, metaData);

		looger.debug(StringUtils.format("returning controller bean instance '{0}'", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	@Override
	public MethodInvokeCallback proxify(BeanObject<?> bean, ControllerMetaData<?> metaData) throws ConfigurationException {

		/*
		 * Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME,
		 * Proxifier.class);
		 * 
		 * CrudController<?> controller = (CrudController<?>) bean;
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
		 * DataModelFactory.class, CrudService.class, ControllerMetaData.class);
		 * } catch (NoSuchMethodException | SecurityException e) { throw new
		 * ConfigurationException(e); }
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
		return new ControllerListenerMethodInterceptor((CrudController<?>) bean, container);
	}
}
