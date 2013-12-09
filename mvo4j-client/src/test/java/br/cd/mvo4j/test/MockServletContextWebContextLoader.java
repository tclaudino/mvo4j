package br.cd.mvo4j.test;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class MockServletContextWebContextLoader extends AbstractContextLoader {

	@Override
	public final ConfigurableApplicationContext loadContext(String... locations)
			throws Exception {

		return loadContext(null, locations);
	}

	@Override
	protected String getResourceSuffix() {
		return "-context.xml";
	}

	@Override
	public ApplicationContext loadContext(
			MergedContextConfiguration mergedConfig) throws Exception {

		return loadContext(mergedConfig.getParentApplicationContext(),
				mergedConfig.getLocations());
	}

	private ConfigurableApplicationContext loadContext(
			ApplicationContext parent, String... locations) throws Exception {
		ConfigurableWebApplicationContext context = new XmlWebApplicationContext();

		MockServletContext servletContext = new MockServletContext(
				"src/test/webapp", new FileSystemResourceLoader());

		context.setServletContext(servletContext);
		servletContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				context);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
				request));

		context.setConfigLocations(locations);
		if (parent != null)
			context.setParent(parent);
		context.refresh();
		WebApplicationContextUtils.registerWebApplicationScopes(
				context.getBeanFactory(), servletContext);
		AnnotationConfigUtils
				.registerAnnotationConfigProcessors((BeanDefinitionRegistry) context
						.getBeanFactory());
		context.registerShutdownHook();
		return context;
	}

}
