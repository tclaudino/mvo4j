package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.core.proxy.WebControllerProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.orm.RelationMapBean;
import br.com.cd.scaleframework.orm.suport.ServiceFacade;
import br.com.cd.scaleframework.proxy.ProxyFactory;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.controller.WebController;

public class DefaultWebControllerComponent<C extends WebControllerComponentConfig, P extends WebControllerProxy>
		extends AbstractWebControllerComponent<C> implements
		ControllerComponent<C> {

	private final Class<P> proxyType;

	public DefaultWebControllerComponent(Class<P> proxyType, C config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>... autowireCandidates) {
		super(config, annotationsAttributes, autowireCandidates);
		this.proxyType = proxyType;
	}

	public DefaultWebControllerComponent(Class<P> proxyType, C config,
			Class<?>... autowireCandidates) {
		super(config, autowireCandidates);
		this.proxyType = proxyType;
	}

	@Override
	protected Class<P> createProxy(ComponentBeanFactory applicationContext) {

		String beanName = this.getComponentConfig().getBeanName();

		System.out.println("\n" + this.getClass().getName()
				+ " | registering bean, beanName: " + beanName
				+ ", proxyBeanName: " + this.getComponentConfig().getName());

		if (this.getTargetService() != null) {
			try {

				@SuppressWarnings("rawtypes")
				ProxyFactory<ServiceFacade> factory = ProxyFactory
						.createProxyFactory(ServiceFacade.class);

				// factory.setThisClass(this.getTargetService()
				// .getComponentConfig().getTargetComponent());
				//
				// applicationContext.registerBean(this.getTargetService()
				// .getComponentConfig().getTargetComponent().getName(),
				// factory.getProxyClass());

			} catch (Exception e) {
				throw new BeanCreationException(
						"Can'not create proxy for target '"
								+ this.getTargetService().getComponentConfig(),
						e);
			}
		}

		ProxyFactory<P> factory = ProxyFactory.createProxyFactory(proxyType);

		if (this.getTargetController() != null) {

			// factory.setThisClass(this.getTargetController()
			// .getComponentConfig().getTargetComponent());
		}

		System.out.println("\nmaking List..., list to make: "
				+ this.getComponentConfig().getMakeList());
		for (String beanNameToMake : this.getComponentConfig().getMakeList()
				.trim().split(",")) {

			if (beanNameToMake.isEmpty()) {
				continue;
			}

			beanNameToMake = beanNameToMake.trim();

			System.out.println("beanName: " + beanNameToMake);

			if (!applicationContext.containsBean(beanNameToMake)) {
				throw new ConfigurationException("The bean '"
						+ this.getComponentConfig().getName()
						+ "' has a invalid makeList attribute.",
						new NoSuchBeanDefinitionException(beanNameToMake));
			}

			String mtName = StringUtils.toCammelCase(beanNameToMake) + "List";

			factory.addGetterAndSetter(mtName, List.class);
		}

		Class<?> entityClass = this.getComponentConfig().getTargetEntity();

		System.out.println("scanning relation fields of entityClass: "
				+ entityClass.getName());

		for (RelationMapBean relationMap : this.getComponentConfig()
				.getRelationMaps()) {

			Class<?> target = relationMap.getTargetClass();
			System.out.println("checking relation field : "
					+ relationMap.getAttrListName() + ", target: " + target);

			factory.addGetterAndSetter(
					"current"
							+ StringUtils.toBeanConvencionCase(relationMap
									.getEachName()), target);

			factory.addRelationship(relationMap.getEachName(), target,
					relationMap.getAttrListName(), entityClass, "currentEntity");

		}

		// Adding annotations
		Iterator<Entry<Class<Annotation>, Map<String, Object>>> iterator = this
				.getAnnotationsAttributes().entrySet().iterator();
		while (iterator.hasNext()) {

			Entry<Class<Annotation>, Map<String, Object>> annotationEntry = iterator
					.next();

			System.out.println(this.getClass().getName()
					+ " | attempt to add @"
					+ annotationEntry.getKey().getName()
					+ " in this proxyBean, beanName: " + beanName + ", bean: "
					+ this + ", annotation values: "
					+ annotationEntry.getValue() + ", beanType: "
					+ WebController.class);

			factory.addAnnotation(annotationEntry.getKey(),
					annotationEntry.getValue());

		}

		try {

			Class<P> annotatedProxyType = factory.getProxyClass();

			System.out.println("\n\nstoring proxyfied  bean: "
					+ annotatedProxyType + ", in spring context with name: "
					+ this.getComponentConfig().getName() + "\n\n\n");

			applicationContext.registerBean(
					this.getComponentConfig().getName(), annotatedProxyType);

			if (this.getTargetController() != null) {

				applicationContext.registerBean(this.getTargetController()
						.getComponentConfig().getName(), annotatedProxyType);
			}

			return annotatedProxyType;

		} catch (Exception e) {
			throw new BeanCreationException("Can'not create proxy for target '"
					+ this.proxyType, e);
		}
	}
}
