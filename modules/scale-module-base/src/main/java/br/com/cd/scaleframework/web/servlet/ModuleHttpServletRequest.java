package br.com.cd.scaleframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface ModuleHttpServletRequest extends HttpServletRequest {

	public String getModulePathInfo();

}
