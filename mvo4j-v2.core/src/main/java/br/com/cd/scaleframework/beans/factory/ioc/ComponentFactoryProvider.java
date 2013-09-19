package br.com.cd.scaleframework.beans.factory.ioc;

import javax.servlet.ServletContext;

public interface ComponentFactoryProvider {

	ComponentFactoryContainer getContainer(ServletContext servletContext);

}
