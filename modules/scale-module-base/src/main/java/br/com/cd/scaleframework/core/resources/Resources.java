package br.com.cd.scaleframework.core.resources;

import java.util.LinkedHashMap;
import java.util.Map;

public class Resources {

	public static class ResourceInfo {

		private final String internalPath;
		private final String filePath;
		private final String uri;

		public ResourceInfo(String internalPath, String filePath, String uri) {
			this.internalPath = internalPath;
			this.filePath = filePath;
			this.uri = uri;
		}

		public String getInternalPath() {
			return internalPath;
		}

		public String getFilePath() {
			return filePath;
		}

		public String getUri() {
			return uri;
		}

	}

	private final Map<String, ResourceInfo> resourcesInfo = new LinkedHashMap<String, ResourceInfo>();
	private final ResourceType resourceType;

	public Resources(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public Map<String, ResourceInfo> getPaths() {
		return resourcesInfo;
	}

	public Resources addPath(String internalPath, String filePath, String uri) {
		this.resourcesInfo.put(uri, new ResourceInfo(internalPath, filePath,
				uri));
		return this;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

}
