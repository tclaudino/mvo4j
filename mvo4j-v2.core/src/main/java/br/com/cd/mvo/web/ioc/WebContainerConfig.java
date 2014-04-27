package br.com.cd.mvo.web.ioc;

import javax.servlet.ServletContext;

import br.com.cd.mvo.ioc.AbstractContainerConfig;
import br.com.cd.mvo.web.util.WebUtil;

public class WebContainerConfig extends AbstractContainerConfig<ServletContext> {

	public WebContainerConfig(ServletContext servletContext) {
		super(servletContext, new WebContainerListener());
	}

	@Override
	public String getInitParameter(String key) {

		return WebUtil.getInitParameter(localContainer, key);
	}

}
