package br.com.cd.scaleframework.core.discovery.support;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
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
import br.com.cd.scaleframework.util.StringUtils;

public class ScannedComponentDiscovery implements ComponentDiscovery {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <O extends ComponentObject, P extends ComponentProxy<O>, A extends Annotation, F extends ComponentListableFactory<O, P, A>, S extends ComponentDiscoverySupport<O, P, A, F>> void discover(
			F componentFactory, S discoverySupport,
			ApplicationContext applicationContext,
			ComponentFactorySupport factorySupport,
			ComponentDiscoveryVisitor registryVisitor) {

		System.out.println("\nscanning classes from packages: '"
				+ factorySupport.getPackagesToScan() + "' return if empty");

		if (StringUtils.isNullOrEmpty(factorySupport.getPackagesToScan())) {
			return;
		}

		FilterBuilder filter = new FilterBuilder();
		for (String pkg : factorySupport.getPackagesToScan().split(",")) {
			filter.include(pkg.replace(".", "\\."));
		}

		Set<Class<?>> classesToScan = new Reflections(
				applicationContext.createConfiguration(filter,
						new TypeAnnotationsScanner()))
				.getTypesAnnotatedWith(Entity.class);

		if (classesToScan.size() == 0) {
			System.out.println("class definition with annotation @"
					+ Entity.class.getName() + " not found in packages '"
					+ factorySupport.getPackagesToScan() + "'");
			return;
		}
		System.out.println("found classes definition with annotation @"
				+ Entity.class.getName() + " in packages '"
				+ factorySupport.getPackagesToScan() + "', classes: "
				+ classesToScan);

		Iterator<Class<?>> iterator = classesToScan.iterator();

		O component;
		Class classToScan;
		String beanName;
		while (iterator.hasNext()) {
			classToScan = iterator.next();

			beanName = (classToScan.getSimpleName().substring(0, 1)
					.toLowerCase() + classToScan.getClass().getSimpleName()
					.substring(1)).replace("Entity", "");
			beanName += "Bean";

			Set<Field> fields = new Reflections(
					applicationContext.createConfiguration(new FilterBuilder()
							.includePackage(classToScan.getClass()),
							new FieldAnnotationsScanner()))
					.getFieldsAnnotatedWith(Id.class);

			if (fields.size() == 0) {
				System.out.println("field definition with annotation @"
						+ Id.class.getName() + " not found in class '"
						+ classToScan.getName() + "'");
				continue;
			}
			System.out.println("found fields definition with annotation @"
					+ Id.class.getName() + " in class '"
					+ classToScan.getName() + "', fields: " + fields);

			Field field = new ArrayList<Field>(fields).get(0);
			if (!Serializable.class.isAssignableFrom(field.getDeclaringClass())) {
				System.out.println("field definition '" + field.getName()
						+ " in class '" + classToScan.getName()
						+ " is not Serializable");
				return;
			}

			if (componentFactory.containsComponent(classToScan)) {
				System.out.println("proxy for '" + classToScan.getName()
						+ "' already registered");
				continue;
			}

			try {
				classToScan.newInstance();
			} catch (Exception e) {
				throw new BeanException(e);
			}

			component = discoverySupport.createComponent(beanName,
					classToScan.getName(), applicationContext, factorySupport);

			component.getComponentConfig().setScope(
					ParserUtils.parseString(factorySupport.getScope()));

			component.getComponentConfig().setEntityIdType(
					(Class<Serializable>) field.getDeclaringClass());

			componentFactory.registerComponent(component, applicationContext);

			discoverySupport.onRegistryComponent(component, applicationContext);

			component.accept(registryVisitor);
		}
	}
}
