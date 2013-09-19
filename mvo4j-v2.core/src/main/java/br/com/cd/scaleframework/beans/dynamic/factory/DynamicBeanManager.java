package br.com.cd.scaleframework.beans.dynamic.factory;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;

public class DynamicBeanManager<Config extends BeanConfig<?, ?>> {

	private final Class<?> targetBean;
	private final Config beanConfig;

	public DynamicBeanManager(Class<?> targetBean, Config beanConfig) {
		this.targetBean = targetBean;
		this.beanConfig = beanConfig;
	}

	public Class<?> getTargetBean() {
		return targetBean;
	}

	public Config getBeanConfig() {
		return beanConfig;
	}

}
