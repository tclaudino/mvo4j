package br.com.cd.scaleframework.beans.dynamic.factory;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;

public class DynamicBean<Config extends BeanConfig<?, ?>> {

	private final Class<?> targetBean;
	private final Class<?> targetEntity;
	private final Config beanConfig;

	public DynamicBean(Class<?> targetBean, Class<?> targetEntity,
			Config beanConfig) {
		this.targetBean = targetBean;
		this.targetEntity = targetEntity;
		this.beanConfig = beanConfig;
	}

	public Class<?> getTargetBean() {
		return targetBean;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public Config getBeanConfig() {
		return beanConfig;
	}

}
