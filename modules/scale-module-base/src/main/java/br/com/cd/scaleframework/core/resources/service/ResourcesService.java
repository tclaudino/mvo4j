package br.com.cd.scaleframework.core.resources.service;

import br.com.cd.scaleframework.core.resources.ResourceType;
import br.com.cd.scaleframework.core.resources.Resources;

public interface ResourcesService {

	Resources getResources(ResourceType resourceType);

	String getResource(String resourceName);

	ResourcesService addResources(ResourceType resourceType,
			String internalPath, String filePath, String uri);

	void compress(String compressionFolder);

	void copyResources(String compressionFolder);

	void copyResources(String resourcesFolder, boolean compress);

}
