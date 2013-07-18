package br.com.cd.scaleframework.core.discovery.support;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
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

public class AnnotatedComponentDiscovery implements ComponentDiscovery {

	@SuppressWarnings("rawtypes")
	@Override
	public <O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> void discover(
			F componentFactory, S discoverySupport,
			ApplicationContext applicationContext,
			ComponentFactorySupport factorySupport,
			ComponentDiscoveryVisitor registryVisitor) {

		System.out.println("\nsearching classes annotated with @"
				+ discoverySupport.getAnnotationType().getName());

		Set<Class<?>> classes = new Reflections(
				applicationContext.createConfiguration(new FilterBuilder(),
						new TypeAnnotationsScanner()))
				.getTypesAnnotatedWith(discoverySupport.getAnnotationType());

		if (classes.size() == 0) {
			System.out.println("class definition with annotation @"
					+ discoverySupport.getAnnotationType().getName()
					+ " not found.");
			return;
		}
		System.out.println("found classes annotated with @"
				+ discoverySupport.getAnnotationType().getName() + ": "
				+ classes);

		Iterator<Class<?>> iterator = classes.iterator();
		while (iterator.hasNext()) {

			Class<?> controllerClass = iterator.next();

			String sessionFactoryQualifier = ParserUtils.parseString(
					factorySupport.getSessionFactoryQualifier(),
					ComponentFactorySupport.SESSION_FACTORY_QUALIFIER);

			A annotation = controllerClass.getAnnotation(discoverySupport
					.getAnnotationType());

			sessionFactoryQualifier = ParserUtils.parseString(
					discoverySupport.getSessionFactoryQualifier(annotation),
					sessionFactoryQualifier);

			System.out.println("controllerClass: " + controllerClass.getName()
					+ ", annotation: " + annotation
					+ ", sessionFactoryQualifier: " + sessionFactoryQualifier);

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

			final O component = discoverySupport.createComponent(annotation,
					applicationContext, factorySupport);

			componentFactory.registerComponent(component, applicationContext);

			discoverySupport.onRegistryComponent(component, applicationContext);

			component.accept(registryVisitor);
		}
	}

}
