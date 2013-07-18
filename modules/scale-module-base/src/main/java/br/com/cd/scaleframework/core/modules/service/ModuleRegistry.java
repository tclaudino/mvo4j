package br.com.cd.scaleframework.core.modules.service;

import br.com.cd.scaleframework.core.modules.InvalidModuleException;
import br.com.cd.scaleframework.core.modules.ModuleDefinition;

public interface ModuleRegistry {

	void registerModule(Class<?> controller, ModuleDefinition module)
			throws InvalidModuleException;

	void unregisterModule(ModuleDefinition module);
}
