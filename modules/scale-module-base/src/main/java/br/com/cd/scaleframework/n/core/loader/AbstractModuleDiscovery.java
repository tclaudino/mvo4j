package br.com.cd.scaleframework.n.core.loader;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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
import org.springframework.web.context.WebApplicationContext;

import br.com.cd.scaleframework.core.modules.ModuleBean;
import br.com.cd.scaleframework.core.modules.service.ModuleRegistry;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.n.core.ModuleDefinition;

public class AbstractModuleDiscovery implements ModuleDiscovery {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractModuleDiscovery.class);

	public static final String MODULES_APPLICATION_CONTEXT_RESOURCE_NAME = "modules-context.xml";

	private ModuleRegistry moduleRegistry;

	private WebApplicationContext context;

	@Override
	public void dicoverModules() {

		Map<Class<?>, ModuleBean> activeModules = new LinkedHashMap<Class<?>, ModuleBean>();

		Set<URL> urls = new HashSet<URL>(ClasspathHelper.forJavaClassPath());
		urls.add(ClasspathHelper.forWebInfClasses(context.getServletContext()));
		urls.addAll(ClasspathHelper.forWebInfLib(context.getServletContext()));
		urls.addAll(ClasspathHelper.forManifest());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(new FilterBuilder()).setUrls(urls)
				.setScanners(new TypeAnnotationsScanner()));

		Set<Class<?>> modulesClasses = reflections.getTypesAnnotatedWith(Module.class);
		for (Class<?> moduleClass : modulesClasses) {

			String jarFilePath = "file:"
					+ moduleClass.getProtectionDomain().getCodeSource()
							.getLocation().getPath() + "!/";

			final ModuleDefinition moduleDefinition = new ModuleDefinition(
					moduleClass.getAnnotation(Module.class), moduleClass);

			reflections = new Reflections(new ConfigurationBuilder()
			.filterInputsBy(new FilterBuilder()).setUrls(new URL(""))
			.setScanners(new ResourcesScanner()));
			
			Set<String> resources=  reflections.getResources(Pattern.compile(MODULES_APPLICATION_CONTEXT_RESOURCE_NAME));
			
			if (resources.size() > 0) {
				if (resources.size() > 1) {
					
				}		
							}
			
			
		}

		context.getClassLoader()
	}
}
