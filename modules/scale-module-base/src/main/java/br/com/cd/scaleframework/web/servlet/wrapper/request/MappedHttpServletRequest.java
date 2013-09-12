package br.com.cd.scaleframework.web.servlet.wrapper.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import br.com.cd.scaleframework.web.servlet.ModuleHttpServletRequest;
import br.com.cd.scaleframework.web.servlet.wrapper.RequestModuleMapping;

public class MappedHttpServletRequest extends HttpServletRequestWrapper
		implements ModuleHttpServletRequest {

	private static final Log logger = LogFactory
			.getLog(MappedHttpServletRequest.class);

	private ServletContext servletContext;

	private Map<String, String> uriToPathInfo;

	private String contextPathPlusServletPath;

	private String mappingServletPath;

	private String mappingContextPath;

	public MappedHttpServletRequest(ServletContext servletContext,
			HttpServletRequest request, RequestModuleMapping moduleMapping,
			String applicationId) {

		super(request);

		Assert.notNull(servletContext, "servletContext cannot be null");

		this.servletContext = servletContext;

		if (moduleMapping != null) {

			final String mappingServletPath = moduleMapping.getServletPath();
			final String mappingContextPath = moduleMapping.getContextPath();
			if (mappingServletPath != null || mappingContextPath != null) {
				this.contextPathPlusServletPath = request.getContextPath()
						+ (mappingContextPath != null ? mappingContextPath : "")
						+ (mappingServletPath != null ? mappingServletPath : "");
			}
			this.mappingServletPath = mappingServletPath;
			this.mappingContextPath = mappingContextPath;

			// force empty mapping servlet path if context path is set
			if (mappingContextPath != null && mappingServletPath == null) {
				this.mappingServletPath = "";
			}
		}
	}

	/**
	 * Returns the wrapped context path (see
	 * {@link HttpServletRequest#getContextPath()}, plus the mapped context path
	 * (see {@link #mappingContextPath}.
	 */
	@Override
	public String getContextPath() {
		String contextPath = super.getContextPath();
		if (mappingContextPath != null) {
			contextPath += mappingContextPath;
		}

		return contextPath;
	}

	/**
	 * If valid servlet path is provided, then this is returned as long the
	 * request is currently not in a forward or include. Otherwise, delegates to
	 * wrapped {@link #getServletPath()}
	 */
	public String getServletPath() {
		if (mappingServletPath == null || isForwardOrInclude()) {
			final String servletPath = super.getServletPath();
			return servletPath;
		}

		return mappingServletPath;
	}

	/**
	 * If valid servlet path is provided, the remainder of URI is returned.
	 * Otherwise, delegates to wrapped {@link #getPathInfo()}
	 */
	@Override
	public String getPathInfo() {
		if (mappingServletPath == null || isForwardOrInclude()) {
			final String pathInfo = super.getPathInfo();
			return pathInfo;
		}

		return getModulePathInfo();
	}

	/**
	 * Returns the path info calculated from the servlet URI, less the context
	 * path and the module path.
	 */
	public String getModulePathInfo() {
		String pathInfo;
		if (uriToPathInfo == null) {
			uriToPathInfo = new HashMap<String, String>();
		}

		final String uri = this.getRequestURI();
		pathInfo = uriToPathInfo.get(uri);
		if (pathInfo == null) {
			pathInfo = getPathInfo(uri);
			uriToPathInfo.put(uri, pathInfo);
		}
		return pathInfo;
	}

	/**
	 * If valid servlet path is provided, then real path associated with path
	 * info is returned. Otherwise, delegates to wrapped {@link #getPathInfo()}.
	 * 
	 * See {@link ServletContext#getRealPath(String)}
	 */
	@Override
	public String getPathTranslated() {
		String pathInfo = getPathInfo();
		if (pathInfo != null) {
			return servletContext.getRealPath(pathInfo);
		}
		return super.getPathTranslated();
	}

	/* ****************** Helper methods ****************** */

	boolean isForwardOrInclude() {
		if (getAttribute("javax.servlet.forward.request_uri") != null
				|| getAttribute("javax.servlet.include.request_uri") != null) {
			return true;
		}
		return false;
	}

	String getPathInfo(String uri) {
		String pathInfo = null;

		if (uri.startsWith(this.contextPathPlusServletPath)) {
			pathInfo = uri.substring(this.contextPathPlusServletPath.length());
		} else {
			logger.warn("URI does not start with context plus servlet path combination: "
					+ this.contextPathPlusServletPath);
			final String contextPath = getContextPath();
			if (contextPath != null && uri.startsWith(contextPath)) {
				pathInfo = uri.substring(contextPath.length());
			} else {
				logger.warn("URI does not start with context path: "
						+ contextPath);
				pathInfo = uri;
			}
		}
		return pathInfo;
	}

}