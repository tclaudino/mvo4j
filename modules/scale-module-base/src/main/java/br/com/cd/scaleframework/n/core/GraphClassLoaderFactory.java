package br.com.cd.scaleframework.n.core;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.util.Assert;

public class GraphClassLoaderFactory implements ClassLoaderFactory {

	private ClassLoaderRegistry classLoaderRegistry;

	public GraphClassLoaderFactory(ClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}

	@Override
	public ClassLoader newClassLoader(ClassLoader parent,
			ModuleDefinition moduleDefinition) {

		if (classLoaderRegistry.getApplicationClassLoader() == null) {
			classLoaderRegistry.setApplicationClassLoader(parent);
		}
		
		new XmlBeanFactory(resource)

		Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");

		return newClassLoader(moduleDefinition);
	}

	private ClassLoader newClassLoader(ModuleDefinition moduleDefinition) {

		String moduleName = moduleDefinition.getName();
		ClassLoader classLoader = classLoaderRegistry
				.getClassLoader(moduleName);
		if (classLoader != null) {
			return classLoader;
		}
		
		classLoader = URLClassLoader.newInstance(moduleDefinition.get, classLoaderRegistry.getApplicationClassLoader());

		List<ModuleDefinition> dependencies = dependencyManager
				.getOrderedModuleDependencies(moduleDefinition.getName());

		List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
		for (ModuleDefinition dependency : dependencies) {
			if (dependency.getName().equals(moduleDefinition.getName()))
				continue;
			classLoaders.add(newClassLoader(dependency));
		}

		ClassLoader parentClassLoader = classLoaderRegistry
				.getApplicationClassLoader();
		ClassLoader parentClassLoaderToUse = parentClassLoader != null ? parentClassLoader
				: GraphClassLoaderFactory.class.getClassLoader();

		ClassLoader gcl = new Class;
		classLoaderRegistry.addClassLoader(moduleDefinition.getName(), gcl);
		return gcl;
	}
}
