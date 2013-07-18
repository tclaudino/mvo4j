package br.com.cd.scaleframework.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class ModuleContextLoaderListener implements ServletContextListener {

	public static final String PACKAGES_TO_SCAN = "br.com.cd";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		System.out.println("\n\ncontextInitialized");

		ConfigurableWebApplicationContext applicationContext = new DefaultSpringLocator()
				.getApplicationContext(servletContextEvent.getServletContext());

		System.out.println("\n\napplicationContext: " + applicationContext);

		applicationContext.setServletContext(servletContextEvent
				.getServletContext());

		applicationContext
				.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

					@Override
					public void postProcessBeanFactory(
							ConfigurableListableBeanFactory beanFactory)
							throws BeansException {

						DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;

						AnnotationConfigUtils
								.registerAnnotationConfigProcessors(listableBeanFactory);
						AopConfigUtils
								.registerAspectJAnnotationAutoProxyCreatorIfNecessary(listableBeanFactory);

						doScan(listableBeanFactory);
					}
				});

		applicationContext.refresh();
	}

	public void doScan(final BeanDefinitionRegistry beanRegistry) {

		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
				beanRegistry);

		scanner.setIncludeAnnotationConfig(true);
		System.out.println("\n\nApplicationContextAware scaning packages: "
				+ PACKAGES_TO_SCAN);

		scanner.scan(PACKAGES_TO_SCAN);
	}

}
