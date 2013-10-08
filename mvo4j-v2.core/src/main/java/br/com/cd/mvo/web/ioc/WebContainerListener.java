package br.com.cd.mvo.web.ioc;

import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerListener;
import br.com.cd.mvo.ioc.support.BeanFactoryComponentFactory;
import br.com.cd.mvo.ioc.support.ControllerBeanFactory;

public class WebContainerListener implements ContainerListener {

	@Override
	public void contextLoaded(Container container) {

		BeanFactory<?, ?> bf = new ControllerBeanFactory(container);
		ComponentFactory<BeanFactory<?, ?>> cf = new BeanFactoryComponentFactory<BeanFactory<?, ?>>(
				container, bf);

		container.addComponentFactory(cf);
		container.registerSingleton(bf.getClass().getName(), cf);

		bf = new WebControllerBeanFactory(container);
		cf = new BeanFactoryComponentFactory<BeanFactory<?, ?>>(container, bf);

		container.addComponentFactory(cf);
		container.registerSingleton(bf.getClass().getName(), cf);
	}

	@Override
	public void setup(Container container) {

		// nothing?
	}

}
