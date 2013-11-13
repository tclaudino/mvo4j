package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
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
	public Collection<Class<?>> scan(Class<? extends Annotation> annotationType, String[] packagesToScan) {

		ConfigurationBuilder configuration = new ConfigurationBuilder().filterInputsBy(getFilterBuilder(packagesToScan)).setUrls(getURLs())
				.setScanners(new TypeAnnotationsScanner());

		return new Reflections(configuration).getTypesAnnotatedWith(annotationType);
	}

	protected FilterBuilder getFilterBuilder(String... packagesToScan) {
		FilterBuilder filterBuilder = new FilterBuilder();
		for (String pkg : packagesToScan) {
			filterBuilder.include(FilterBuilder.prefix(pkg));
		}
		return filterBuilder;
	}

	protected Set<URL> getURLs() {

		Set<URL> urls = new HashSet<URL>();

		addAll(urls, ClasspathHelper.forJavaClassPath());
		addAll(urls, ClasspathHelper.forManifest());

		ServletContext sc = (ServletContext) container.getContainerConfig().getLocalContainer();

		if (container.getContainerConfig().getLocalContainer() instanceof ServletContext) {

			if (sc.getResourcePaths("/WEB-INF/lib") != null) addAll(urls, ClasspathHelper.forWebInfLib(sc));

			if (sc.getResourcePaths("/WEB-INF/classes") != null) {
				URL tempURL = ClasspathHelper.forWebInfClasses(sc);
				if (tempURL != null) {
					urls.add(tempURL);
				}
			}
		}

		return urls;
	}

	private void addAll(Set<URL> urls, Set<URL> tempURLs) {
		if (tempURLs != null && tempURLs.size() > 0) urls.addAll(tempURLs);
	}

	@Override
	public <T> Collection<Class<? extends T>> scanSubTypesOf(Class<T> type, String[] packagesToScan) {

		ConfigurationBuilder configuration = new ConfigurationBuilder().filterInputsBy(getFilterBuilder(packagesToScan)).setUrls(getURLs())
				.setScanners(new SubTypesScanner());

		return new Reflections(configuration).getSubTypesOf(type);
	}

}
