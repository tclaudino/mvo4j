package br.com.cd.scaleframework.core.resources;

public class DynamicResourceType implements ResourceType {

	private final String extension;
	private final String mimeType;

	public DynamicResourceType(String extension, String mimeType) {
		this.extension = extension;
		this.mimeType = mimeType;
	}

	@Override
	public String getExtension() {
		return extension;
	}

	@Override
	public String getMimeType() {
		return this.mimeType;
	}

	public static ResourceType getResourceType(final String ext) {
		for (ResourceType resourceType : StaticResourceType.values()) {
			if (resourceType.getExtension().equals(ext)) {
				return resourceType;
			}
		}
		return new DynamicResourceType(ext, "");
	}

	@Override
	public String toString() {
		return "[extension=" + extension + ", mimeType=" + mimeType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((extension == null) ? 0 : extension.hashCode());
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
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
		DynamicResourceType other = (DynamicResourceType) obj;
		if (extension == null) {
			if (other.extension != null)
				return false;
		} else if (!extension.equals(other.extension))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		return true;
	}

}
