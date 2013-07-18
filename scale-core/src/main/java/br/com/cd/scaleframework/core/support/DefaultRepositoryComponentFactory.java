package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.RepositoryComponentFactory;
import br.com.cd.scaleframework.core.dynamic.RepositoryBean;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;
import br.com.cd.scaleframework.core.proxy.support.DefaultRepositoryProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

@SuppressWarnings("rawtypes")
public class DefaultRepositoryComponentFactory
		extends
		AbstractComponentFactory<RepositoryComponent, RepositoryProxy<?>, RepositoryBean>
		implements RepositoryComponentFactory {

	@Override
	protected Map<Class<? extends Annotation>, Map<String, Object>> createAnnotationsAttributes(
			RepositoryComponent component) {

		Map<Class<? extends Annotation>, Map<String, Object>> annotations = new HashMap<Class<? extends Annotation>, Map<String, Object>>();

		annotations.put(Component.class, new HashMap<String, Object>());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("value", component.getComponentConfig().getScope());

		annotations.put(Scope.class, params);

		return annotations;
	}

	@Override
	protected RepositoryProxy<?> getComponent(RepositoryComponent component,
			ComponentBeanFactory applicationContext) {

		DefaultRepositoryProxy<?> componentProxy = null;

		// // DefaultRepositoryComponent componentProxy =
		// component.getComponent();
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
		// if (component.getComponentProvider()
		// .getSessionFactory(applicationContext)
		// .getClass().isAssignableFrom(parameterType)) {
		// args.add(component.getComponentProvider()
		// .getSessionFactory(applicationContext));
		// } else {
		// args.add(applicationContext.getBean(parameterType));
		// }
		// }
		// }
		// componentProxy = applicationContext.getBean(overrideBeanName,
		// DefaultRepositoryProxy.class, args);
		// }
		// }

		return componentProxy;
	}

}
