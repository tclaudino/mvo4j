package br.com.cd.scaleframework.core.modules.service;

import java.util.Collection;
import java.util.Map;

import br.com.cd.scaleframework.core.modules.ModuleDefinition;
import br.com.cd.scaleframework.core.modules.ModuleDependent;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.module.PublishEventType;

public interface ModuleFactory {

	Collection<ModuleDefinition> getActiveModules();

	ModuleDefinition getActiveModule(Module module);

	boolean isActive(ModuleDefinition module);

	boolean isActive(String id);

	Map<String, ModuleDefinition> getActiveModulesMap();

	Collection<ModuleDependent> getDependentModules(Module module,
			PublishEventType publishEvent);
}
