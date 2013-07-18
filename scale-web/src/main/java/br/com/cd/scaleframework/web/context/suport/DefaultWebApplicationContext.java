package br.com.cd.scaleframework.web.context.suport;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import br.com.cd.scaleframework.ioc.BeanFactoryFacade;
import br.com.cd.scaleframework.ioc.BeanRegistryFacade;

public class DefaultWebApplicationContext extends AbstractWebApplicationContext {

	private final BeanRegistryFacade beanRegistry;

	public DefaultWebApplicationContext(BeanRegistryFacade beanRegistry,
			BeanFactoryFacade beanFactory, ServletContext servletContext) {
		super(beanRegistry, beanFactory, servletContext);

		this.beanRegistry = beanRegistry;
	}

	@Override
	public BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanRegistry.getBeanDefinitionRegistry();
	}

}