package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.DefaultApplication;
import br.com.cd.mvo.ioc.Container;

public class ApplicationComponentFactory extends
		AbstractComponentFactory<Application> {

	public ApplicationComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected Application getInstanceInternal() {

		return new DefaultApplication();
	}

	@Override
	protected String getComponentBeanName() {
		return Application.BEAN_NAME;
	}
}
