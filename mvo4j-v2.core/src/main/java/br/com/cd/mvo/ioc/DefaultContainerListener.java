package br.com.cd.mvo.ioc;

import br.com.cd.mvo.ioc.support.BeanFactoryComponentFactory;
import br.com.cd.mvo.ioc.support.ControllerBeanFactory;

public class DefaultContainerListener implements ContainerListener {

	@Override
	public void configure(Container container) {

		BeanFactory<?, ?> bf = new ControllerBeanFactory(container);

		ComponentFactory<BeanFactory<?, ?>> cf = new BeanFactoryComponentFactory<BeanFactory<?, ?>>(
				container, bf);

		container.addComponentFactory(cf);
		container.registerSingleton(bf.getClass().getName(), cf);
	}
}
