package br.com.cd.scaleframework.orm.suport;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.RepositoryComponentConfig;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.util.GenericsUtils;

public abstract class AbstractSessionFactoryProvider<Factory> implements
		SessionFactoryProvider<Factory> {

	private final Class<Factory> factoryClass;

	@SuppressWarnings("rawtypes")
	private RepositoryComponent component;

	@SuppressWarnings("unchecked")
	public AbstractSessionFactoryProvider() {
		this.factoryClass = GenericsUtils.getTypeArguments(
				AbstractSessionFactoryProvider.class, this.getClass()).get(0);
	}

	public AbstractSessionFactoryProvider(Class<Factory> factoryClass) {
		this.factoryClass = factoryClass;
	}

	@Override
	public Factory getSessionFactory(ComponentBeanFactory applicationContext) {
		return applicationContext.getBean(
				((RepositoryComponentConfig) component.getComponentConfig())
						.getSessionFactoryQualifier(), this.factoryClass);
	}

	@Override
	public void setComponent(
			@SuppressWarnings("rawtypes") RepositoryComponent component) {
		this.component = component;
	}

}