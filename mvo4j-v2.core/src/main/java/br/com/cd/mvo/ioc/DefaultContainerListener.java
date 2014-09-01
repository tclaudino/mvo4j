package br.com.cd.mvo.ioc;

import br.com.cd.mvo.ioc.scan.ControllerListenerMetaDataFactory;

public class DefaultContainerListener implements ContainerListener {

	@Override
	public void configure(Container container) {

		// nothing?
	}

	@Override
	public void deepRegister(Container container) {

		container.addBeanFactory(new ControllerBeanFactory(container));
		container.addBeanFactory(new NullInstanceBeanFactory<>(container, new ControllerListenerMetaDataFactory()));
	}
}
