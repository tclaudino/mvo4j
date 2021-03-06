package br.com.cd.mvo.ioc;


public class BeanFactoryComponentFactory<F extends BeanFactory<?, ?>> extends AbstractComponentFactory<F> {

	private F beanFactory;

	public BeanFactoryComponentFactory(Container container, F beanFactory) {
		super(container);
		this.beanFactory = beanFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<F> getComponentType() {
		return (Class<F>) beanFactory.getClass();
	}

	@Override
	protected F getInstanceInternal() throws NoSuchBeanDefinitionException {

		return beanFactory;
	}

	@Override
	protected String getComponentBeanName() {
		return this.getComponentType().getName();
	}

}