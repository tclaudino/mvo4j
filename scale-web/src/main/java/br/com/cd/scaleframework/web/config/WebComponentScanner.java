package br.com.cd.scaleframework.web.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.cd.scaleframework.config.support.AbstractComponentScanner;
import br.com.cd.scaleframework.web.context.WebApplicationContext;

public class WebComponentScanner extends
		AbstractComponentScanner<WebApplicationContext> {

	@Override
	protected void onStartScan(WebApplicationContext applicationContext) {

		BeanDefinitionRegistry beanRegistry = applicationContext
				.getBeanDefinitionRegistry();
		if (beanRegistry instanceof ConfigurableListableBeanFactory) {
			WebApplicationContextUtils
					.registerWebApplicationScopes((ConfigurableListableBeanFactory) beanRegistry);
		}

		// spring-mvc
		// registerPostProcessor(beanRegistry,
		// DefaultAnnotationHandlerMapping.class);
		// registerPostProcessor(beanRegistry,
		// AnnotationMethodHandlerAdapter.class);
		// registerPostProcessor(beanRegistry, SimpleUrlHandlerMapping.class);
		// registerPostProcessor(beanRegistry, BeanNameUrlHandlerMapping.class);
		// registerPostProcessor(beanRegistry,
		// ServletRequestAwareProcessor.class);
	}
}