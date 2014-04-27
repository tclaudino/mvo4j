package br.com.cd.mvo.web.ioc;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.DefaultContainerListener;

public class WebContainerListener extends DefaultContainerListener {

	@Override
	public void configure(Container container) {

		// nothing?
		super.configure(container);
	}

	@Override
	public void deepRegister(Container container) {

		super.deepRegister(container);

		container.addBeanFactory(new WebControllerBeanFactory(container));
	}
}
