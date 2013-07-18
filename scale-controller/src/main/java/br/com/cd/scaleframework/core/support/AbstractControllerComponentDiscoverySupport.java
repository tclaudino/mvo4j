package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.FilterBuilder;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.controller.EventListener;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.core.ControllerComponentConfig;
import br.com.cd.scaleframework.core.ControllerComponentDiscoverySupport;
import br.com.cd.scaleframework.core.ControllerComponentFactory;
import br.com.cd.scaleframework.core.proxy.ControllerProxy;
import br.com.cd.scaleframework.orm.RelationMap;
import br.com.cd.scaleframework.orm.RelationMapBean;
import br.com.cd.scaleframework.util.GenericsUtils;

@SuppressWarnings("rawtypes")
public abstract class AbstractControllerComponentDiscoverySupport<O extends ControllerComponent, P extends ControllerProxy<O>, A extends Annotation, F extends ControllerComponentFactory<O, P, A>>
		implements ControllerComponentDiscoverySupport<O, P, A, F> {

	private Set<Class<? extends EventListener>> listeners;

	@SuppressWarnings("unchecked")
	protected void setListeners(O component,
			ApplicationContext applicationContext) {
		System.out.println("\nfind controller listeners");

		Set<Class<? extends EventListener>> classes = getListeners(applicationContext);

		System.out.println("loaded listeners classes: " + classes);
		if (classes.size() == 0) {
			return;
		}

		for (Class<? extends EventListener> listenerClass : classes) {

			Class<?> targetEntity = GenericsUtils.getTypeArguments(
					EventListener.class, listenerClass).get(0);

			System.out.println("checking if found '" + listenerClass.getName()
					+ "' at target [" + targetEntity.getName() + "] is a '"
					+ component.getComponentConfig().getTargetEntity()
					+ "' listener");

			if (component.getComponentConfig().getTargetEntity()
					.isAssignableFrom(targetEntity)) {
				System.out.println("found listener '"
						+ listenerClass.getName()
						+ "' for targetEntity '"
						+ component.getComponentConfig().getTargetEntity()
								.getName() + "'");

				if (!((ControllerComponentConfig) component
						.getComponentConfig()).getListeners().contains(
						listenerClass)) {
					((ControllerComponentConfig) component.getComponentConfig())
							.addListener(listenerClass);
				}
			}
		}
	}

	protected Set<Class<? extends EventListener>> getListeners(
			ApplicationContext applicationContext) {
		if (listeners == null) {

			listeners = new Reflections(applicationContext.createConfiguration(
					new FilterBuilder(), new SubTypesScanner()))
					.getSubTypesOf(EventListener.class);
		}
		return listeners;
	}

	protected void addRelationMaps(O beanProxy, RelationMap[] relationMaps) {

		if (relationMaps == null || relationMaps.length == 0) {
			addRelationMaps(beanProxy);
		} else {
			addRelationMaps(beanProxy, Arrays.asList(relationMaps));
		}
	}

	protected void addRelationMaps(O beanProxy, List<RelationMap> relationMaps) {

		if (relationMaps == null || relationMaps.isEmpty()) {
			addRelationMaps(beanProxy);
			return;
		}

		for (RelationMap relationMap : relationMaps) {
			Class target = relationMap.targetClass();
			if (target == null || target.equals(Void.class)) {
				try {
					Field field = relationMap.targetClass().getField(
							relationMap.attrListName());
					List<Class> genericParams = GenericsUtils
							.getTypeArguments((ParameterizedType) field
									.getGenericType());
					if (genericParams.size() > 0) {
						target = genericParams.get(0);
					}
				} catch (Exception e) {
					//
				}
			}
			if (target == null) {
				throw new ConfigurationException(
						"target not defined from relationMap '" + relationMap
								+ "' in bean '"
								+ beanProxy.getComponentConfig().getBeanName()
								+ "'");
			}
			System.out.println("addRelationMaps, attrName: "
					+ relationMap.eachName() + ", target: " + target.getName());

			beanProxy.getComponentConfig().addRelationMap(
					new RelationMapBean(relationMap.attrListName(), relationMap
							.eachName(), target));
		}
	}

	protected void addRelationMaps(O beanProxy) {
		for (Field field : beanProxy.getComponentConfig().getTargetEntity()
				.getDeclaredFields()) {
			if (Collection.class.isAssignableFrom(field.getType())) {
				String attrName = field.getName().replace("List", "")
						.replace("list", "");
				if (attrName.endsWith("ies")) {
					attrName = attrName.substring(0, attrName.length() - 5);
				} else if (attrName.endsWith("s")) {
					attrName = attrName.substring(0, attrName.length() - 3);
				}
				System.out.println("addRelationMaps, attrName: " + attrName);

				List<Class> genericParams = GenericsUtils
						.getTypeArguments((ParameterizedType) field
								.getGenericType());
				if (genericParams.size() > 0) {
					Class target = genericParams.get(0);

					System.out.println("target: " + target.getName());

					beanProxy.getComponentConfig().addRelationMap(
							new RelationMapBean(field.getName(), attrName,
									target));
				}
			}
		}
	}

}
