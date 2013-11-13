package br.com.cd.mvo.ioc;

import br.com.cd.mvo.ioc.support.ControllerBeanFactory;
import br.com.cd.mvo.ioc.support.ControllerListenerBeanFactory;

public class DefaultContainerListener implements ContainerListener {

	@Override
	public void configure(Container container) {

		// nothing?
	}

	@Override
	public void deepRegister(Container container) {

		container.addComponentFactory(new ControllerBeanFactory(container));
		container.addComponentFactory(new ControllerListenerBeanFactory(container));
	}
}
