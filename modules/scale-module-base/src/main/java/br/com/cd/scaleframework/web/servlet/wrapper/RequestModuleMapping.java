package br.com.cd.scaleframework.web.servlet.wrapper;

public class RequestModuleMapping {
	/**
	 * The name of the module to which the request is mapped
	 */
	private final String moduleName;

	/**
	 * The part of the URL which is responsible for mapping the request to the
	 * module.
	 */
	private final String moduleMappingPath;

	/**
	 * The servlet path which should be used when module this module mapping
	 * applies {@link javax.servlet.http.HttpServletRequest#getServletPath()}
	 */
	private String servletPath;

	/**
	 * The extra string which should be appended to the context path. Applies to
	 * {@link javax.servlet.http.HttpServletRequest#getContextPath()}
	 */
	private String contextPath;

	public RequestModuleMapping(String path, String moduleName,
			String contextPath, String servletPath) {
		super();
		this.moduleName = moduleName;
		this.moduleMappingPath = path;
		this.contextPath = contextPath;
		this.servletPath = servletPath;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String getModuleMappingPath() {
		return moduleMappingPath;
	}

	public String getServletPath() {
		return servletPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contextPath == null) ? 0 : contextPath.hashCode());
		result = prime
				* result
				+ ((moduleMappingPath == null) ? 0 : moduleMappingPath
						.hashCode());
		result = prime * result
				+ ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result
				+ ((servletPath == null) ? 0 : servletPath.hashCode());
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
		RequestModuleMapping other = (RequestModuleMapping) obj;
		if (contextPath == null) {
			if (other.contextPath != null)
				return false;
		} else if (!contextPath.equals(other.contextPath))
			return false;
		if (moduleMappingPath == null) {
			if (other.moduleMappingPath != null)
				return false;
		} else if (!moduleMappingPath.equals(other.moduleMappingPath))
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		if (servletPath == null) {
			if (other.servletPath != null)
				return false;
		} else if (!servletPath.equals(other.servletPath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequestModuleMapping [moduleMappingPath=" + moduleMappingPath
				+ ", moduleName=" + moduleName + ", contextPath=" + contextPath
				+ ", servletPath=" + servletPath + "]";
	}

}
