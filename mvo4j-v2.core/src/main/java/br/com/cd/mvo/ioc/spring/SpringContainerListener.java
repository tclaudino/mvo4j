package br.com.cd.mvo.ioc.spring;

import org.springframework.context.ConfigurableApplicationContext;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.DefaultContainerListener;

public class SpringContainerListener extends DefaultContainerListener {

	// private ConfigurableApplicationContext parentApplicationContext;

	public SpringContainerListener(
			ConfigurableApplicationContext parentApplicationContext) {
		// this.parentApplicationContext = parentApplicationContext;
	}

	@Override
	public void setup(Container container) {

		// nothing?
	}
}
