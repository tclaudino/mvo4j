package br.com.cd.scaleframework.core.modules;

import java.lang.reflect.Method;

public class ModuleDependent {

	private final ModuleDefinition module;

	private final ModuleDefinition dependOn;

	private final Method method;

	private final String selector;

	public ModuleDependent(ModuleDefinition module, ModuleDefinition dependOn,
			Method method, String selector) {
		this.module = module;
		this.dependOn = dependOn;
		this.method = method;
		this.selector = selector;
	}

	public ModuleDefinition getModule() {
		return module;
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
