package br.com.cd.scaleframework.core.discovery.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.FilterBuilder;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.BeanException;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentListableFactory;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.discovery.ComponentDiscovery;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoverySupport;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;
import br.com.cd.scaleframework.core.proxy.ComponentProxy;
import br.com.cd.scaleframework.util.ParserUtils;

public class AutoProxyfieComponentDiscovery implements ComponentDiscovery {

	@SuppressWarnings("rawtypes")
	@Override
	public <O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> void discover(
			F componentFactory, S discoverySupport,
			ApplicationContext applicationContext,
			ComponentFactorySupport factorySupport,
			ComponentDiscoveryVisitor registryVisitor) {

		System.out.println("\nsearching methods annotated with @"
				+ discoverySupport.getAnnotationType().getName());

		Set<Method> methods = new Reflections(
				applicationContext.createConfiguration(new FilterBuilder(),
						new MethodAnnotationsScanner()))
				.getMethodsAnnotatedWith(discoverySupport.getAnnotationType());

		if (methods.size() == 0) {
			System.out.println("method definition with annotation @"
					+ discoverySupport.getAnnotationType().getName()
					+ " not found.");
			return;
		}
		System.out.println("found methods annotated with @"
				+ discoverySupport.getAnnotationType().getName() + ": "
				+ methods);

		Iterator<Method> iterator = methods.iterator();
		while (iterator.hasNext()) {

			Method method = iterator.next();
			System.out.println("found method: " + method);

			String sessionFactoryQualifier = ComponentFactorySupport.SESSION_FACTORY_QUALIFIER;

			System.out.println("sessionFactoryQualifier: "
					+ sessionFactoryQualifier);

			A annotation = method.getAnnotation(discoverySupport
					.getAnnotationType());

			sessionFactoryQualifier = ParserUtils.parseString(
					discoverySupport.getSessionFactoryQualifier(annotation),
					sessionFactoryQualifier);

			System.out.println("method: " + method + ", annotation: "
					+ annotation + ", sessionFactoryQualifier: "
					+ sessionFactoryQualifier);

			Class<?> targetEntity = discoverySupport
					.getTargetEntity(annotation);
			if (componentFactory.containsComponent(targetEntity)) {
				System.out.println("proxy for '" + targetEntity.getName()
						+ "' already registered");
				continue;
			}

			try {
				targetEntity.newInstance();
			} catch (Exception e) {
				throw new BeanException(e);
			}

			O component = discoverySupport.createComponent(annotation,
					applicationContext, factorySupport);

			component.accept(registryVisitor);

			componentFactory.registerComponent(component, applicationContext);

			discoverySupport.onRegistryComponent(component, applicationContext);
		}
	}

}
