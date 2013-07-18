package br.com.cd.scaleframework.web.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cd.scaleframework.context.DefaultSpringLocator;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;
import br.com.cd.scaleframework.util.StringUtils;

@WebFilter(filterName = "resourceModuleFilter", urlPatterns = { "/modules/resources/*" }, dispatcherTypes = {
		DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE })
public class ResourceModuleFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(ResourceModuleFilter.class);

	public static final String RESOURCES_PATH = "/resources";

	private ResourcesService resourcesService;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String resourceName = this
				.extractResourceName((HttpServletRequest) request);

		String resourceFile = resourcesService.getResource(resourceName);

		if (StringUtils.isNullOrEmpty(resourceFile)) {
			chain.doFilter(request, response);
			return;
		}

		this.writeResource(resourceFile, response);
	}

	private void writeResource(String resourceName, ServletResponse response)
			throws IOException {
		try {
			URL urlIn = new URL(resourceName);

			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(new File(urlIn.toURI())));
			ServletOutputStream out = response.getOutputStream();

			try {

				byte[] buf = new byte[in.available()];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} finally {
				in.close();
				out.flush();
			}

		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	private String extractResourceName(HttpServletRequest request) {

		return request.getServletPath().replace("/resources", "");
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		this.resourcesService = new DefaultSpringLocator()
				.getApplicationContext(config.getServletContext()).getBean(
						ResourcesService.class);

	}

}
