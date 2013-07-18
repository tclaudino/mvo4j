package br.com.cd.scaleframework.web.servlet;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.ContextLoaderListener;

import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.util.ParserUtils;

public class ModuleFilter implements Filter {

	public static final String SPRING_CONFIG_RESOURCE = "modules-context.xml";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		String contextConfigLocation = ParserUtils.parseString(config
				.getServletContext().getAttribute("contextConfigLocation"));

		if (!contextConfigLocation.isEmpty()) {
			contextConfigLocation += "\n" + SPRING_CONFIG_RESOURCE;
		}

		config.getServletContext().setAttribute("contextConfigLocation",
				contextConfigLocation);

		try {
			ContextLoaderListener listener = config.getServletContext()
					.createListener(ContextLoaderListener.class);
			if (listener == null) {
				config.getServletContext().addListener(
						ContextLoaderListener.class);
			}
		} catch (Throwable e) {
			config.getServletContext().addListener(ContextLoaderListener.class);
		}

		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(
				DispatcherType.FORWARD, DispatcherType.REQUEST);

		Dynamic dynamic = config.getServletContext().addFilter(
				ResourceModuleFilter.class.getName(),
				ResourceModuleFilter.class);

		for (StaticResourceType resourceType : StaticResourceType.values()) {
			dynamic.addMappingForUrlPatterns(dispatcherTypes, false, "*"
					+ resourceType.getExtension());
		}
	}
}
