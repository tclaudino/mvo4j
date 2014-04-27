package br.com.cd.mvo.ioc;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.core.DefaultApplication;

public class ApplicationComponentFactory extends AbstractComponentFactory<Application> {

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
