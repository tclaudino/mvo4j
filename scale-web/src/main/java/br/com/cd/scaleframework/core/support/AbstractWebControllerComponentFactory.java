package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.core.ControllerComponentFactory;
import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.proxy.ControllerProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.util.ReflectionUtils;
import br.com.cd.scaleframework.util.StringUtils;

public abstract class AbstractWebControllerComponentFactory<O extends ControllerComponent<?>, P extends ControllerProxy<O>, A extends Annotation>
		extends AbstractComponentFactory<O, P, A> implements
		ControllerComponentFactory<O, P, A> {

	private int initialPageSize = -1;

	private final Class<P> proxyType;

	public AbstractWebControllerComponentFactory(Class<P> proxyType) {
		this.proxyType = proxyType;
	}

	public AbstractWebControllerComponentFactory(Class<P> proxyType,
			Map<String, O> components) {
		super(components);
		this.proxyType = proxyType;
	}

	@Override
	public int getInitialPageSize() {
		return initialPageSize;
	}

	@Override
	public void setInitialPageSize(int initialPageSize) {
		this.initialPageSize = initialPageSize;
	}

	@Override
	protected P getComponent(O component,
			ComponentBeanFactory applicationContext) {

		P proxyBean = applicationContext.getBean(component.getComponentConfig()
				.getBeanName(), this.proxyType);

		System.out.println("\nloaded from spring context, bean: " + proxyBean);
		proxyBean.setComponent(component);

		@SuppressWarnings("rawtypes")
		ServiceComponent serviceComponent = component.getTargetService();

		// TODO:
		if (serviceComponent == null) {
			try {
				// serviceComponent = applicationContext.getComponent(component
				// .getComponentConfig().getTargetEntity(),
				// ServiceComponent.class);
			} catch (NoSuchBeanDefinitionException e) {
				// TODO Create Dynamic Proxy
			}
		}

		// serviceComponent.getComponentConfig().getBeanName()
		//
		// ServiceProxy serviceProxy =
		// applicationContext.getBean(serviceComponent
		// .getComponentConfig().getName(), ServiceProxy.class, args);
		//
		// ServiceFacade service =
		// serviceComponent.getProxy(applicationContext);
		//
		// serviceProxy.getRepositoryProxy();

		// Repository repository = service.getRepository();

		for (String beanNameToMake : component.getComponentConfig()
				.getMakeList().trim().split(",")) {

			if (beanNameToMake.isEmpty()) {
				continue;
			}
			beanNameToMake = beanNameToMake.trim();

			@SuppressWarnings("rawtypes")
			Controller toMakeBean = applicationContext.getBean(beanNameToMake,
					Controller.class);

			String mtName = "set"
					+ StringUtils.toBeanConvencionCase(beanNameToMake) + "List";

			try {
				ReflectionUtils.getObject(proxyBean, proxyBean.getClass()
						.getMethod(mtName, List.class), toMakeBean
						.getEntityList());
			} catch (Exception e) {
				throw new ConfigurationException("The bean '"
						+ component.getComponentConfig().getName()
						+ "' has a inválid makeList attribute.", e);
			}
		}

		{
			String overrideBeanName = component.getTargetService()
					.getComponentConfig().getName();
			if (applicationContext.containsBean(overrideBeanName)) {

				// List<Object> args = new LinkedList<Object>();
				// for (Constructor<?> constructor :
				// component.getTargetService()
				// .getComponentConfig().getTargetComponent()
				// .getConstructors()) {
				//
				// for (Class<?> parameterType : constructor
				// .getParameterTypes()) {
				// if (service.getClass().isAssignableFrom(parameterType)) {
				// args.add(service);
				// } else if (repository.getClass().isAssignableFrom(
				// parameterType)) {
				// args.add(repository);
				// } else if (Translator.class
				// .isAssignableFrom(parameterType)) {
				// args.add(proxyBean.getTranslator());
				// } else if (Messenger.class
				// .isAssignableFrom(parameterType)) {
				// args.add(proxyBean.getMessenger());
				// } else {
				// args.add(applicationContext.getBean(parameterType));
				// }
				// }
				// }
				// service = applicationContext.getBean(overrideBeanName,
				// ServiceFacade.class, args);
				//
				// proxyBean.setService(service);
			}
		}

		{
			String overrideBeanName = component.getTargetController()
					.getComponentConfig().getName();
			if (applicationContext.containsBean(overrideBeanName)) {

				List<Object> args = new LinkedList<Object>();
				// for (Constructor<?> constructor : component
				// .getTargetController().getComponentConfig()
				// .getTargetComponent().getConstructors()) {
				//
				// for (Class<?> parameterType : constructor
				// .getParameterTypes()) {
				// if (proxyType.isAssignableFrom(parameterType)) {
				// args.add(proxyBean);
				// } else if (service.getClass().isAssignableFrom(
				// parameterType)) {
				// args.add(service);
				// } else if (repository.getClass().isAssignableFrom(
				// parameterType)) {
				// args.add(repository);
				// } else if (Translator.class
				// .isAssignableFrom(parameterType)) {
				// args.add(proxyBean.getTranslator());
				// } else if (Messenger.class
				// .isAssignableFrom(parameterType)) {
				// args.add(proxyBean.getMessenger());
				// } else {
				// args.add(applicationContext.getBean(parameterType));
				// }
				// }
				// }
				proxyBean = applicationContext.getBean(overrideBeanName,
						this.proxyType, args);
			}
		}

		return proxyBean;
	}

}