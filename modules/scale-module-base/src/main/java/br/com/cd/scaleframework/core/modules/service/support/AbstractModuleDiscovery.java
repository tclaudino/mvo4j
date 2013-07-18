package br.com.cd.scaleframework.core.modules.service.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import br.com.cd.scaleframework.core.modules.InvalidModuleException;
import br.com.cd.scaleframework.core.modules.ModuleDefinition;
import br.com.cd.scaleframework.core.modules.service.ModuleDiscovery;
import br.com.cd.scaleframework.core.modules.service.ModuleFactoryRegistry;
import br.com.cd.scaleframework.core.resources.ResourceType;
import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;
import br.com.cd.scaleframework.module.Module;

import com.google.common.base.Predicate;

public abstract class AbstractModuleDiscovery implements ModuleDiscovery {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractModuleDiscovery.class);

	private ModuleFactoryRegistry registry;

	private WebApplicationContext context;

	private ResourcesService resourcesService;

	public AbstractModuleDiscovery(ModuleFactoryRegistry registry,
			ResourcesService resourcesService, WebApplicationContext context) {
		this.registry = registry;
		this.resourcesService = resourcesService;
		this.context = context;

		this.dicoverModules();
	}

	@Override
	public void dicoverModules() {

		Map<Class<?>, ModuleDefinition> activeModules = new LinkedHashMap<Class<?>, ModuleDefinition>();

		Set<URL> urls = new HashSet<URL>(ClasspathHelper.forJavaClassPath());
		urls.add(ClasspathHelper.forWebInfClasses(context.getServletContext()));
		urls.addAll(ClasspathHelper.forWebInfLib(context.getServletContext()));
		urls.addAll(ClasspathHelper.forManifest());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(new FilterBuilder()).setUrls(urls)
				.setScanners(new TypeAnnotationsScanner()));

		Set<Class<?>> modules = reflections.getTypesAnnotatedWith(Module.class);

		for (Class<?> moduleType : modules) {

			final ModuleDefinition moduleBean = new ModuleDefinition(
					moduleType.getAnnotation(Module.class), moduleType);

			String jarFilePath = "file:"
					+ moduleType.getProtectionDomain().getCodeSource()
							.getLocation().getPath() + "!/";

			reflections = new Reflections(new ConfigurationBuilder()
					.filterInputsBy(new FilterBuilder()).setUrls(urls)
					.setScanners(new TypeAnnotationsScanner()));

			Set<String> resources = reflections.getResources(Pattern
					.compile(""));

			if (!resources.isEmpty()) {
				if (resources.size() > 0) {

				}

				String resource = resources.toArray(new String[1])[0];

				try {
					URLClassLoader urlClassLoader = new URLClassLoader(
							new URL[] { new URL(jarFilePath) },
							context.getClassLoader());

					XmlBeanFactory beanFactory = new XmlBeanFactory(
							new UrlResource(jarFilePath + resource));

					GenericWebApplicationContext genericWebApplicationContext = new GenericWebApplicationContext(
							beanFactory, context.getServletContext());

					genericWebApplicationContext.setParent(context);

				} catch (Exception e) {
					logger.error("", e);
				}
			}

			// for copping jsp's
			// String pkg = this.extractPackage(moduleBean,
			// moduleBean.getViewsFolder());
			//
			// this.addResources(moduleBean, jarFilePath, new
			// DynamicResourceType(
			// moduleBean.getViewsExtension()), pkg);

			for (StaticResourceType resourceType : StaticResourceType.values()) {

				if (resourceType.equals(StaticResourceType.NONE)) {
					continue;
				}

				Collection<String> pkgs = this.extractPackages(moduleBean,
						resourceType);

				this.addResources(moduleBean, jarFilePath, resourceType,
						pkgs.toArray(new String[pkgs.size()]));

			}
			activeModules.put(moduleType, moduleBean);
		}

		this.registerModules(activeModules);
	}

	private void addResources(final ModuleDefinition moduleBean,
			String jarFilePath, ResourceType resourceType, final String... pkgs) {

		Set<URL> urls = new HashSet<URL>();
		try {
			urls.add(new URL("jar:" + jarFilePath));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		final String regex = "[^.]*("
				+ resourceType.getExtension().replaceAll("[,;]+", "|")
						.replaceAll("\\.", "\\\\.") + ")";

		ConfigurationBuilder configuration = new ConfigurationBuilder()
				.filterInputsBy(
						new FilterBuilder().add(new Predicate<String>() {

							@Override
							public boolean apply(String input) {
								for (String pkg : pkgs) {
									if (Pattern.matches(
											pkg.replaceAll("/", "\\\\.")
													+ "\\." + regex, input))
										return true;
								}
								return false;
							}

						})).setScanners(new ResourcesScanner());

		configuration.setUrls(urls);
		Reflections reflections = new Reflections(configuration);

		// regex = "(#replace)" + regex;
		// for (String pkg : pkgs) {
		// regex = regex.replace("#replace", pkg + "|#replace");
		// }
		// regex = regex.replace("|#replace", "");

		Set<String> paths = reflections.getResources(Pattern.compile(regex));

		if (paths.isEmpty()) {
			return;
		}

		for (String path : paths) {
			String destPath = "/" + moduleBean.getName()
					+ ("/" + path).replace(moduleBean.getResourcesFolder(), "");
			resourcesService.addResources(resourceType, "/" + path, jarFilePath
					+ path, destPath);
		}
	}

	private Collection<String> extractPackages(ModuleDefinition moduleBean,
			StaticResourceType resourceType) {

		Collection<String> pkgs = new LinkedHashSet<String>();

		Collection<String> paths = moduleBean.getResources(resourceType);
		for (String path : paths) {

			pkgs.add(this.extractPackage(moduleBean, path));
		}

		if (pkgs.isEmpty()) {
			pkgs.add(moduleBean.getResourceClass().getPackage().getName());
		}
		return pkgs;
	}

	private String extractPackage(ModuleDefinition moduleBean, String path) {

		path = path.startsWith("/") ? path : (moduleBean.getResourcesFolder()
				+ "/" + path);
		if (path.startsWith("/")) {
			path = path.substring(1);
		} else {
			path = moduleBean.getResourceClass().getPackage().getName()
					.replaceAll("\\.", "/")
					+ "/" + path;
		}

		return path;
	}

	protected void registerModule(Class<?> controllerType,
			ModuleDefinition module) throws InvalidModuleException {

		if (!this.registry.isActive(module)) {
			this.registry.registerModule(controllerType, module);
		}
	}

	protected void registerModules(Map<Class<?>, ModuleDefinition> modules) {

		Iterator<Entry<Class<?>, ModuleDefinition>> iterator = modules
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Class<?>, ModuleDefinition> entry = iterator.next();

			try {
				this.registerModule(entry.getKey(), entry.getValue());
			} catch (InvalidModuleException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
