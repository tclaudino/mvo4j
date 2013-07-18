package br.com.cd.scaleframework.config;

import br.com.cd.scaleframework.context.ApplicationContext;

public interface ComponentScanner<C extends ApplicationContext> {

	void init(C applicationContext);
}