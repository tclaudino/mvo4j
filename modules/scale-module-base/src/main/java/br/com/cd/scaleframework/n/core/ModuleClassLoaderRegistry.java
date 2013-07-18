package br.com.cd.scaleframework.n.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class ModuleClassLoaderRegistry implements ClassLoaderRegistry {

	private ClassLoader applicationClassLoader;

	private Map<String, ClassLoader> classLoaders = new HashMap<String, ClassLoader>();

	public ClassLoader getApplicationClassLoader() {
		return applicationClassLoader;
	}

	public void setApplicationClassLoader(ClassLoader applicationClassLoader) {
		this.applicationClassLoader = applicationClassLoader;
	}

	public ModuleClassLoaderRegistry(ClassLoader classLoader) {
		this.applicationClassLoader = classLoader;
	}

	public ClassLoader getClassLoader(String moduleName) {
		checkModuleName(moduleName);
		return classLoaders.get(moduleName);
	}

	private void checkModuleName(String moduleName) {
		Assert.notNull(moduleName, "moduleName cannot be null");
	}

	public void addClassLoader(String moduleName, ClassLoader classLoader) {
		checkModuleName(moduleName);
		Assert.notNull(classLoader, "classLoader cannot be null");
		synchronized (classLoaders) {
			classLoaders.put(moduleName, classLoader);
		}
	}

	public ClassLoader removeClassLoader(String moduleName) {
		checkModuleName(moduleName);
		synchronized (classLoaders) {
			return classLoaders.remove(moduleName);
		}
	}

}
