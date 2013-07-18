package br.com.cd.scaleframework.n.core.loader;

import java.util.Collection;
import java.util.Map;

import br.com.cd.scaleframework.core.modules.ActiveModule;
import br.com.cd.scaleframework.module.PublishEventType;
import br.com.cd.scaleframework.n.core.ModuleDefinition;
import br.com.cd.scaleframework.n.core.ModuleDependentDefinition;
import br.com.cd.scaleframework.n.core.RuntimeModule;

public interface ModuleFactory {

	Collection<RuntimeModule> getActiveModules();

	ActiveModule getActiveModule(ModuleDefinition module);

	boolean isActive(RuntimeModule module);

	boolean isActive(String id);

	Map<String, RuntimeModule> getActiveModulesMap();

	Collection<ModuleDependentDefinition> getDependentModules(
			ModuleDefinition module, PublishEventType publishEvent);
}
