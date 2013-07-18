package br.com.cd.scaleframework.n.core;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModuleJspServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ServletContext servletContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.servletContext = config.getServletContext();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final String prefix = (String) req
				.getAttribute(WebAttributeQualifier.MODULE_QUALIFIER_PREFIX);
		if (prefix == null) {
			throw new IllegalStateException(
					"Cannot resolve attribute '"
							+ WebAttributeQualifier.MODULE_QUALIFIER_PREFIX
							+ "'."
							+ " This attribute needs to be set in order for ModuleJspServlet to be able to find the module registered JSP servlet to which the request will be forwarded."
							+ " Possible causes: 1) you have not set the property 'partitioned.servlet.context=true' in impala.properties, or "
							+ " 2) you have attempted to access the JSP directly rather than through a request or forward from a servlet or filter within the application.");
		}

		final HttpServlet jspServlet = (HttpServlet) servletContext
				.getAttribute(prefix + JspConstants.JSP_SERVLET);

		if (jspServlet == null) {
			throw new IllegalStateException(
					"No JSP servlet registered in the module to which the request was directed."
							+ " Your module configuration requires a "
							+ JspServletFactoryBean.class
							+ " configuration entry, or equivalent.");
		}

		jspServlet.service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}
}
