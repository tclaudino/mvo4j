package br.com.cd.mvo.web.ioc;

import javax.servlet.ServletContext;

import br.com.cd.mvo.ioc.AbstractContainerConfig;
import br.com.cd.mvo.web.ioc.scan.WebControllerMetaDataFactory;
import br.com.cd.mvo.web.util.WebUtil;

public class WebApplicationConfig extends
		AbstractContainerConfig<ServletContext> {

	{
		factories.add(new WebControllerMetaDataFactory());
	}

	public WebApplicationConfig(ServletContext servletContext) {
		super(servletContext, new WebContainerListener());
	}

	@Override
	public String getInitParameter(String key) {

		return WebUtil.getInitParameter(localContainer, key);
	}

}
