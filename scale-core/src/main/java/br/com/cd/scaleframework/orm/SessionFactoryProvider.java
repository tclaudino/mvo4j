package br.com.cd.scaleframework.orm;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

public interface SessionFactoryProvider<Factory> {

	Factory getSessionFactory(ComponentBeanFactory applicationContext);

	RepositoryFactory<Factory> getRepositoryFactory();

	void setComponent(
			@SuppressWarnings("rawtypes") RepositoryComponent component);

}