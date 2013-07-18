package br.com.cd.scaleframework.web.context;

import javax.servlet.ServletContext;

import br.com.cd.scaleframework.context.ApplicationContext;

public interface WebApplicationContext extends ApplicationContext {

	ServletContext getServletContext();

}