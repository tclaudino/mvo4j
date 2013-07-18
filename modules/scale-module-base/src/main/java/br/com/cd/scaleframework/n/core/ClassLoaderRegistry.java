package br.com.cd.scaleframework.n.core;

public interface ClassLoaderRegistry {

	ClassLoader getApplicationClassLoader();

	void setApplicationClassLoader(ClassLoader parent);

	void addClassLoader(String moduleName, ClassLoader classLoader);

	ClassLoader getClassLoader(String moduleName);

	ClassLoader removeClassLoader(String moduleName);

}
