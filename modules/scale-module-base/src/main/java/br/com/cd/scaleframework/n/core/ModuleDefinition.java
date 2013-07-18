package br.com.cd.scaleframework.n.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import br.com.cd.scaleframework.core.resources.StaticResource;
import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.module.Module;

public class ModuleDefinition {

	private final String id;
	private final String name;
	private final String version;
	private final Class<?> resourceClass;
	private final String resourcesFolder;
	// private final String viewsFolder;
	// private final String viewsExtension;

	private final Map<StaticResourceType, Collection<String>> resources = new LinkedHashMap<StaticResourceType, Collection<String>>();

	private final Collection<ModuleDependentDefinition> dependents = new LinkedHashSet<ModuleDependentDefinition>();

	public ModuleDefinition(Module module, Class<?> resourceClass) {
		this.id = module.id();
		this.name = module.name();
		this.version = module.version();
		this.resourcesFolder = module.resourcesFolder();
		// this.viewsFolder = module.viewsFolder();
		// this.viewsExtension = module.viewsExtension();
		this.resourceClass = resourceClass;

		for (StaticResource staticResource : module.resources()) {
			if (staticResource.folders().length == 0)
				continue;

			for (StaticResourceType resourceType : staticResource
					.resourceTypes()) {
				this.resources.put(resourceType,
						Arrays.asList(staticResource.folders()));
			}
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Class<?> getResourceClass() {
		return resourceClass;
	}

	public Map<StaticResourceType, Collection<String>> getResources() {
		return resources;
	}

	public Collection<String> getResources(StaticResourceType resourceType) {
		Collection<String> rs = this.resources.get(resourceType);
		return rs != null ? rs : new ArrayList<String>();
	}

	public String getResourcesFolder() {
		return resourcesFolder;
	}

	public Collection<ModuleDependentDefinition> getDependents() {
		return dependents;
	}

	public Collection<ModuleDependentDefinition> addDependent(
			ModuleDependentDefinition depedentModule) {
		dependents.add(depedentModule);
		return dependents;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleDefinition other = (ModuleDefinition) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
