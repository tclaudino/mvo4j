package br.com.scaleframework.vraptor;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.caelum.vraptor.ioc.spring.SpringProvider;
import br.com.cd.scaleframework.config.support.AbstractComponentScanner;
import br.com.cd.scaleframework.ioc.spring.SpringContainer;
import br.com.cd.scaleframework.web.context.WebApplicationContext;
import br.com.cd.scaleframework.web.context.suport.DefaultWebApplicationContext;

public class SpringViewProvider extends SpringProvider {

	// private ComponentRegistry registry;
	//
	// @Override
	// protected void registerCustomComponents(ComponentRegistry registry) {
	// this.registry = registry;
	// }

	private class SpringViewBeanFactoryPostProcessor implements
			BeanFactoryPostProcessor {

		private final ConfigurableWebApplicationContext applicationContext;

		public SpringViewBeanFactoryPostProcessor(
				ConfigurableWebApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		@Override
		public void postProcessBeanFactory(
				final ConfigurableListableBeanFactory beanFactory)
				throws BeansException {

			System.out.println("\n\nPOST PROCESSOR\n");

			if (!(beanFactory instanceof BeanDefinitionRegistry)) {
				throw new ApplicationContextException(
						"Fatal initialization error in '"
								+ AbstractComponentScanner.class.getName()
								+ "' + custom BeanFactory defined in ApplicationContext "
								+ " is not of type BeanDefinitionRegistry");
			}

			SpringContainer container = new SpringContainer(
					(DefaultListableBeanFactory) beanFactory);

			WebApplicationContext appContext = new DefaultWebApplicationContext(
					container, container,
					this.applicationContext.getServletContext());

//			appContext.refresh();

			// System.out.println("\n\n"
			// + appContext
			// .getBean("contractTypeBean",
			// ControllerComponent.class)
			// .getComponentConfig().getName());
		}
	}

	@Override
	protected ConfigurableWebApplicationContext getParentApplicationContext(
			ServletContext context) {

		ConfigurableWebApplicationContext parent = super
				.getParentApplicationContext(context);

		System.out
				.println("SpringViewProvider.getParentApplicationContext, initializing...\napplicationContext: "
						+ parent);

		parent.addBeanFactoryPostProcessor(new SpringViewBeanFactoryPostProcessor(
				parent));

		return parent;
	}
}
