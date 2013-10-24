package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.Container;

public class BeanFactoryComponentFactory<F extends BeanFactory<?, ?>> extends
		AbstractComponentFactory<F> {

	private F componentFactory;

	public BeanFactoryComponentFactory(Container container, F componentFactory) {
		super(container);
		this.componentFactory = componentFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<F> getObjectType() {
		return (Class<F>) componentFactory.getClass();
	}

	@Override
	protected F getInstanceInternal() throws NoSuchBeanDefinitionException {

		return componentFactory;
	}

}