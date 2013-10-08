package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import br.com.cd.mvo.ioc.Container;

public class ReflectionsScanner implements Scanner {

	private final Container container;

	public ReflectionsScanner(Container container) {
		this.container = container;
	}

	@Override
	public Collection<Class<?>> scan(String[] packageToScan,
			Class<? extends Annotation> annotationType) {

		Set<URL> urls = new HashSet<URL>();

		urls.addAll(ClasspathHelper.forJavaClassPath());
		urls.addAll(ClasspathHelper.forManifest());
		if (container.getContainerConfig().getLocalContainer() instanceof ServletContext) {
			urls.addAll(ClasspathHelper.forWebInfLib((ServletContext) container
					.getContainerConfig().getLocalContainer()));
			urls.add(ClasspathHelper
					.forWebInfClasses((ServletContext) container
							.getContainerConfig().getLocalContainer()));
		}

		ConfigurationBuilder configuration = new ConfigurationBuilder()
				.filterInputsBy(new FilterBuilder()).setUrls(urls)
				.setScanners(new TypeAnnotationsScanner());

		return new Reflections(configuration)
				.getTypesAnnotatedWith(annotationType);
	}

}
