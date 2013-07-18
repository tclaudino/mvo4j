package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public abstract class AbstractComponentFactory<O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation>
		implements ComponentListableFactory<O, P, A> {

	private Map<String, O> componentProxies = new LinkedHashMap<String, O>();

	public AbstractComponentFactory() {
	}

	public AbstractComponentFactory(Map<String, O> components) {
		this.componentProxies = components;
	}

	protected abstract Map<Class<? extends Annotation>, Map<String, Object>> createAnnotationsAttributes(
			O component);

	protected abstract P getComponent(O component,
			ComponentBeanFactory applicationContext);

	@SuppressWarnings("unchecked")
	@Override
	public void registerComponent(O proxy,
			ComponentBeanFactory applicationContext) {
		Iterator<Entry<Class<? extends Annotation>, Map<String, Object>>> iterator = createAnnotationsAttributes(
				proxy).entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Class<? extends Annotation>, Map<String, Object>> entry = iterator
					.next();
			proxy.addAnnotation(entry.getKey(), entry.getValue());
		}

		Class<?> proxyClass = proxy.getProxy(applicationContext);

		componentProxies.put(proxy.getComponentConfig().getBeanName(), proxy);

		applicationContext.registerBean(proxy.getComponentConfig().getName(),
				proxyClass);
	}

	@Override
	public boolean containsComponent(String componantName) {
		return componentProxies.containsKey(componantName);
	}

	@Override
	public boolean containsComponent(Class<?> targetEntity) {
		for (O bean : componentProxies.values()) {
			if (bean.getComponentConfig().getTargetEntity()
					.equals(targetEntity)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public P getComponent(Class<?> targetEntity,
			ComponentBeanFactory applicationContext) {
		for (O component : componentProxies.values()) {
			if (component.getComponentConfig().getTargetEntity()
					.equals(targetEntity)) {
				if (this.containsComponent(component.getComponentConfig()
						.getBeanName())) {

					return this.getComponent(component.getComponentConfig()
							.getBeanName(), applicationContext);
				}
			}
		}
		throw new NoSuchBeanDefinitionException("", targetEntity.getName());
	}

	@Override
	public P getComponent(String componentName,
			ComponentBeanFactory applicationContext) {

		if (!this.containsComponent(componentName)) {
			throw new NoSuchBeanDefinitionException(componentName);
		}

		return this.getComponent(this.componentProxies.get(componentName),
				applicationContext);
	}

	// protected <O extends ComponentObject, R extends ComponentProxy<O>> R
	// getComponent(
	// Class<O> targetComponent, Class<?> targetEntity,
	// C applicationContext) {
	//
	// applicationContext.get
	//
	// R result = null;
	// if (components.containsKey(targetEntity)) {
	// List<ComponentObject> list = components.get(targetEntity);
	// for (ComponentObject o : list) {
	// if (targetComponent.isAssignableFrom(o.getClass())) {
	// result = (R) this.getComponent(o.getComponentConfig()
	// .getBeanName(), applicationContext);
	// break;
	// }
	// }
	// }
	// if (result == null) {
	// result = this.createAndRegisterComponentProxy();
	// }
	// return result;
	// }
	//
	// private <O extends ComponentObject, R extends ComponentProxy<O>> R
	// createAndRegisterComponentProxy() {
	//
	// return null;
	// }
	//
	// private Class<?>[] getConstructorTypes() {
	//
	// return null;
	// }

	@Override
	public List<O> getComponents() {
		return new ArrayList<O>(componentProxies.values());
	}

	@Override
	public void setComponents(Map<String, O> components) {
		this.componentProxies = components;
	}

}
