package br.com.cd.mvo.ioc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextHolder implements ApplicationContextAware {

	private static ConfigurableApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		ApplicationContextHolder.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
