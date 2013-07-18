package br.com.cd.scaleframework.core.resources;

public enum StaticResourceType implements ResourceType {

	JS(".js", "text/javascript"), CSS(".css", "text/css"), HTML(".html",
			"text/html"), JPEG(".jpeg", "image/jpeg"), JPG(".jpg", "image/jpeg"), PNG(
			".png", "image/png"), GIF(".gif", "image/gif"), ICO(".ico",
			"image/vnd.microsoft.icon"), JSP(".jsp", "text/html"), NONE("", "");

	private final String mimeType;
	private final String extension;

	private StaticResourceType(String extension, String mimeType) {
		this.extension = extension;
		this.mimeType = mimeType;
	}

	@Override
	public String getExtension() {
		return extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String toString() {
		return "[extension=" + extension + ", mimeType=" + mimeType + "]";
	}

}
