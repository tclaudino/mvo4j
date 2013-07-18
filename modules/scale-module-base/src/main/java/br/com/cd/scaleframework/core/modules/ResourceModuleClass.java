package br.com.cd.scaleframework.core.modules;

import java.util.Collection;
import java.util.LinkedHashSet;

public class ResourceModuleClass {

	private final Class<?> type;

	private final Collection<ResourceModuleMethod> methods = new LinkedHashSet<ResourceModuleMethod>();

	private final Collection<ResourceModuleClass> dependents = new LinkedHashSet<ResourceModuleClass>();

	public ResourceModuleClass(Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	public Collection<ResourceModuleMethod> getMethods() {
		return methods;
	}

	public Collection<ResourceModuleClass> getDependents() {
		return dependents;
	}

}
