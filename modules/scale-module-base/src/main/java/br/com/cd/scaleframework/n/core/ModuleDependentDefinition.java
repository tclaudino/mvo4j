package br.com.cd.scaleframework.n.core;

import java.lang.reflect.Method;

public class ModuleDependentDefinition {

	private final ModuleDefinition moduleDefinition;

	private final ModuleDefinition dependOn;

	private final Method method;

	private final String selector;

	public ModuleDependentDefinition(ModuleDefinition module,
			ModuleDefinition dependOn, Method method, String selector) {
		this.moduleDefinition = module;
		this.dependOn = dependOn;
		this.method = method;
		this.selector = selector;
	}

	public ModuleDefinition getModule() {
		return moduleDefinition;
	}

	public ModuleDefinition getDependOn() {
		return dependOn;
	}

	public Method getMethod() {
		return method;
	}

	public String getSelector() {
		return selector;
	}

}
