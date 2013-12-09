package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.config.ControllerMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.DataModelFactory;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.ControllerMetaDataFactory;
import br.com.cd.mvo.util.StringUtils;

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

		@SuppressWarnings({ "unchecked" })
		DefaultCrudController bean = new DefaultCrudController(container.getBean(Application.BEAN_NAME, Application.class),
				container.getBean(Translator.BEAN_NAME, Translator.class), container.getBean(DataModelFactory.BEAN_NAME,
						DataModelFactory.class), service, metaData);

		looger.debug(StringUtils.format("returning controller bean instance '{0}'", bean));
		looger.debug("...............................................................................");
		return bean;
	}

	/*
	 *
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
	 * (NoSuchBeanDefinitionException e) { // TODO LOG this }
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
