package br.com.cd.scaleframework.ioc;

import br.com.cd.scaleframework.context.ApplicationContext;

public interface BeanFactoryFacadeAware<C extends ApplicationContext> {

	void init(C applicationContext);
}