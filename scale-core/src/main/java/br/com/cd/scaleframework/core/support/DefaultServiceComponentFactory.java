package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.ServiceComponentFactory;
import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.core.proxy.ServiceProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public class DefaultServiceComponentFactory extends
		AbstractComponentFactory<ServiceComponent, ServiceProxy, ServiceBean>
		implements ServiceComponentFactory {

	@Override
	protected Map<Class<? extends Annotation>, Map<String, Object>> createAnnotationsAttributes(
			ServiceComponent proxy) {

		Map<Class<? extends Annotation>, Map<String, Object>> annotations = new HashMap<Class<? extends Annotation>, Map<String, Object>>();

		annotations.put(Component.class, new HashMap<String, Object>());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("value", proxy.getComponentConfig().getScope());

		annotations.put(Scope.class, params);

		return annotations;
	}

	@Override
	protected ServiceProxy getComponent(ServiceComponent component,
			ComponentBeanFactory applicationContext) {

		ServiceProxy componentProxy = null;

		// Class<ServiceProxy> componentType = component
		// .getProxy(applicationContext);
		//
		// RepositoryProxy<RepositoryComponent, Object> repository =
		// componentProxy
		// .getRepositoryProxy();
		//
		//
		// if (repository == null) {
		// try {
		// applicationContext.getComponent(component.getComponentConfig()
		// .getTargetEntity(), RepositoryComponent.class);
		// } catch (NoSuchBeanDefinitionException e) {
		//
		// }
		// }
		//
		// {
		// String overrideBeanName = component.getTargetRepository()
		// .getComponentConfig().getName();
		// if (applicationContext.containsBean(overrideBeanName)) {
		//
		// List<Object> args = new LinkedList<Object>();
		// for (Constructor<?> constructor : component
		// .getTargetRepository().getComponentConfig()
		// .getTargetComponent().getConstructors()) {
		//
		// for (Class<?> parameterType : constructor
		// .getParameterTypes()) {
		// if (repository.getClass().isAssignableFrom(
		// parameterType)) {
		// args.add(repository);
		// } else if (repository.getComponent()
		// .getComponentProvider()
		// .getSessionFactory(applicationContext)
		// .getClass().isAssignableFrom(parameterType)) {
		// args.add(repository.getComponent()
		// .getComponentProvider()
		// .getSessionFactory(applicationContext));
		// } else {
		// args.add(applicationContext.getBean(parameterType));
		// }
		// }
		// }
		// repository = applicationContext.getBean(overrideBeanName,
		// RepositoryProxy.class, args);
		//
		// componentProxy.setRepositoryProxy(repository);
		// }
		// }
		//
		// {
		// String overrideBeanName = component.getTargetService()
		// .getComponentConfig().getName();
		// if (applicationContext.containsBean(overrideBeanName)) {
		//
		// List<Object> args = new LinkedList<Object>();
		// for (Constructor<?> constructor : component.getTargetService()
		// .getComponentConfig().getTargetComponent()
		// .getConstructors()) {
		//
		// for (Class<?> parameterType : constructor
		// .getParameterTypes()) {
		// if (DefaultServiceProxy.class
		// .isAssignableFrom(parameterType)) {
		// args.add(componentProxy);
		// } else if (repository.getClass().isAssignableFrom(
		// parameterType)) {
		// args.add(repository);
		// // } else if (Translator.class
		// // .isAssignableFrom(parameterType)) {
		// // args.add(componentProxy.getTranslator());
		// // } else if (Messenger.class
		// // .isAssignableFrom(parameterType)) {
		// // args.add(componentProxy.getMessenger());
		// } else {
		// args.add(applicationContext.getBean(parameterType));
		// }
		// }
		// }
		// componentProxy = applicationContext.getBean(overrideBeanName,
		// DefaultServiceProxy.class, args);
		// }
		// }

		return componentProxy;
	}

}
