package br.com.cd.mvo.web.ioc;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.AbstractBeanFactory;
import br.com.cd.mvo.util.StringUtils;
import br.com.cd.mvo.web.DefaultWebCrudController;
import br.com.cd.mvo.web.bean.WebControllerBean;
import br.com.cd.mvo.web.bean.config.WebControllerMetaData;
import br.com.cd.mvo.web.ioc.scan.WebControllerMetaDataFactory;

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

		@SuppressWarnings("unchecked")
		DefaultWebCrudController bean = new DefaultWebCrudController(container.getBean(Application.BEAN_NAME, Application.class),
				container.getBean(Translator.BEAN_NAME, Translator.class), container.getBean(DataModelFactory.BEAN_NAME,
						DataModelFactory.class), service, metaData);

		looger.debug(StringUtils.format("returning webController bean instance '{0}'", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" })
	 *
	 * @Override public <T> BeanObject<T> wrap(BeanObject<T> bean) {
	 *
	 * if (!(bean instanceof Listenable)) return bean;
	 *
	 * Listenable<T> controller = (Listenable<T>) bean;
	 *
	 * Collection<ControllerListener> listeners = new ArrayList<>(); try {
	 * listeners = container.getBeansOfType(ControllerListener.class); } catch
	 * (NoSuchBeanDefinitionException e) { }
	 *
	 * if (ControllerListener.class.isAssignableFrom(controller.getClass())) {
	 * listeners.add((ControllerListener) controller); }
	 *
	 * for (ControllerListener listener : listeners) { Class<?> targetEntity =
	 * GenericsUtils.getTypesFor(listener.getClass(),
	 * ControllerListener.class).get(0);
	 *
	 * if (controller.getBeanMetaData().targetEntity().equals(targetEntity)) {
	 *
	 * controller.addListener(listener); } } controller.afterPropertiesSet();
	 *
	 * return controller; }
	 */
}