package br.com.cd.scaleframework.n.core;

public interface ClassLoaderFactory {

	ClassLoader newClassLoader(ClassLoader parent,
			ModuleDefinition moduleDefinition);

}
