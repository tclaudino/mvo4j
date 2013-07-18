package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.RepositoryComponentConfig;

public class DefaultRepositoryComponentConfig extends AbstractComponentConfig
		implements RepositoryComponentConfig {

	private String sessionFactoryQualifier = ComponentFactorySupport.SESSION_FACTORY_QUALIFIER;

	public DefaultRepositoryComponentConfig(String name,
			String targetEntityClassName) {
		super(name, targetEntityClassName);
	}

	public DefaultRepositoryComponentConfig(String name, Class<?> targetEntity) {
		super(name, targetEntity);
	}

	public String getSessionFactoryQualifier() {
		return sessionFactoryQualifier;
	}

	public void setSessionFactoryQualifier(String sessionFactoryQualifier) {
		this.sessionFactoryQualifier = sessionFactoryQualifier;
	}

}