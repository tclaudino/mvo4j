package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.ServiceComponentConfig;

public class DefaultServiceComponentConfig extends AbstractComponentConfig
		implements ServiceComponentConfig {

	public DefaultServiceComponentConfig(String name,
			String targetEntityClassName) {
		super(name, targetEntityClassName);
	}

	public DefaultServiceComponentConfig(String name, Class<?> targetEntity) {
		super(name, targetEntity);
	}

}