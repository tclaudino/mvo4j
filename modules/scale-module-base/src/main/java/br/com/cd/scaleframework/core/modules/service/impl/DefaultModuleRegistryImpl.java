package br.com.cd.scaleframework.core.modules.service.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import br.com.cd.scaleframework.core.modules.InvalidModuleException;
import br.com.cd.scaleframework.core.modules.ModuleDefinition;
import br.com.cd.scaleframework.core.modules.ModuleDependent;
import br.com.cd.scaleframework.core.modules.ModuleValidator;
import br.com.cd.scaleframework.core.modules.NullModuleValidatorImpl;
import br.com.cd.scaleframework.core.modules.service.ModuleFactory;
import br.com.cd.scaleframework.core.modules.service.ModuleFactoryRegistry;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;
import br.com.cd.scaleframework.module.DependentModule;
import br.com.cd.scaleframework.module.DependsOn;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.module.PublishEventType;
import br.com.cd.scaleframework.util.VersionManager;

@Component
public class DefaultModuleRegistryImpl implements ModuleFactoryRegistry,
		ModuleFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(DefaultModuleRegistryImpl.class);

	private ModuleValidator validator;

	private Map<String, ModuleDefinition> activeModules = new LinkedHashMap<String, ModuleDefinition>();

	public DefaultModuleRegistryImpl(ResourcesService resourcesService) {
		this(resourcesService, new NullModuleValidatorImpl());
	}

	public DefaultModuleRegistryImpl(ResourcesService resourcesService,
			ModuleValidator validator) {
		this.validator = validator;
	}

	@Autowired
	public DefaultModuleRegistryImpl(WebApplicationContext context) {

		try {
			this.validator = context.getBean(ModuleValidator.class);
		} catch (NoSuchBeanDefinitionException e) {
			logger.error("Module Validator not implemented. Using a default!");
			this.validator = new NullModuleValidatorImpl();
		}
	}

	@Override
	public Collection<ModuleDefinition> getActiveModules() {
		return activeModules.values();
	}

	@Override
	public Map<String, ModuleDefinition> getActiveModulesMap() {
		return activeModules;
	}

	@Override
	public ModuleDefinition getActiveModule(Module module) {
		return this.activeModules.get(module.id());
	}

	@Override
	public Collection<ModuleDependent> getDependentModules(Module module,
			PublishEventType publishEvent) {
		Collection<ModuleDependent> modules = new LinkedList<ModuleDependent>();

		for (ModuleDefinition activeModule : this.getActiveModules()) {
			for (ModuleDependent dependent : activeModule.getDependents()) {

				DependsOn dependsOn = dependent.getMethod().getAnnotation(
						DependsOn.class);

				for (DependentModule dependentModule : dependsOn.modules()) {

					if (dependsOn.onEventType().equals(publishEvent)
							&& dependentModule.id().equals(module.id())) {
						modules.add(dependent);
					}
				}
			}
		}
		return modules;
	}

	@Override
	public void registerModule(Class<?> controllerClass, ModuleDefinition module)
			throws InvalidModuleException {

		this.registerModule(controllerClass, module, false);
	}

	private <T> void registerModule(Class<T> controllerClass,
			ModuleDefinition moduleDefinition, boolean forceOverride)
			throws InvalidModuleException {

		if (forceOverride || !this.isActive(moduleDefinition.getId())) {

			if (!validator.isValid(moduleDefinition)) {
				throw new InvalidModuleException("module '"
						+ moduleDefinition.getId() + "' is invalid on '"
						+ controllerClass.getName() + "'");
			}

			this.registerDependecies(moduleDefinition);

			this.activeModules.put(moduleDefinition.getId(), moduleDefinition);

		} else {

			ModuleDefinition module = this.activeModules.get(moduleDefinition
					.getId());

			if (module.getId().equals(moduleDefinition.getId())) {
				if (VersionManager.isMajor(moduleDefinition.getVersion(),
						module.getVersion())) {

					this.registerModule(controllerClass, moduleDefinition, true);
				}
			}
		}
	}

	private void registerDependecies(ModuleDefinition activingModule) {
		for (ModuleDefinition activeModule : this.activeModules.values()) {

			if (activeModule.getId().equals(activingModule.getId()))
				continue;

			for (Method method : activeModule.getResourceClass()
					.getDeclaredMethods()) {

				if (method.isAnnotationPresent(DependsOn.class)) {
					DependsOn dependsOn = method.getAnnotation(DependsOn.class);
					for (DependentModule dependentModule : dependsOn.modules()) {

						if (dependentModule.id().equals(activingModule.getId())) {
							activingModule.addDependent(new ModuleDependent(
									activeModule, activingModule, method,
									dependentModule.selector()));
						}
					}
				}
			}

			for (Method method : activingModule.getResourceClass()
					.getDeclaredMethods()) {

				if (method.isAnnotationPresent(DependsOn.class)) {
					DependsOn dependsOn = method.getAnnotation(DependsOn.class);
					for (DependentModule dependentModule : dependsOn.modules()) {

						if (dependentModule.id().equals(activeModule.getId())) {
							activeModule.addDependent(new ModuleDependent(
									activingModule, activeModule, method,
									dependentModule.selector()));
						}
					}
				}
			}
		}
	}

	@Override
	public void unregisterModule(ModuleDefinition module) {
		this.activeModules.remove(module.getId());
	}

	@Override
	public boolean isActive(ModuleDefinition module) {
		return this.isActive(module.getId());
	}

	@Override
	public boolean isActive(String id) {
		return this.activeModules.containsKey(id);
	}

}
