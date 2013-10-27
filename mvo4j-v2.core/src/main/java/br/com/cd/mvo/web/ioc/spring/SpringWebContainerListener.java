package br.com.cd.mvo.web.ioc.spring;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.web.ioc.WebContainerListener;

public class SpringWebContainerListener extends WebContainerListener {

	private ConfigurableWebApplicationContext parentApplicationContext;

	public SpringWebContainerListener(
			ConfigurableWebApplicationContext parentApplicationContext) {
		this.parentApplicationContext = parentApplicationContext;
	}

	@Override
	public void configure(Container container) {

		super.configure(container);

		WebApplicationContextUtils
				.registerWebApplicationScopes(parentApplicationContext
						.getBeanFactory());
	}

	@Override
	public void deepRegister(Container container) {

		// nothing?
		super.deepRegister(container);
	}

}
