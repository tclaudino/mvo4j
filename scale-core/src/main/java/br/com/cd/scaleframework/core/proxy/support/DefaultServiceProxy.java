package br.com.cd.scaleframework.core.proxy.support;

import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.proxy.RepositoryProxy;
import br.com.cd.scaleframework.core.proxy.ServiceProxy;
import br.com.cd.scaleframework.orm.suport.ServiceFacade;

@SuppressWarnings("rawtypes")
public class DefaultServiceProxy extends ServiceFacade implements ServiceProxy {

	private ServiceComponent component;

	private RepositoryProxy repositoryProxy;

	@Override
	public void setComponent(ServiceComponent component) {
		this.component = component;
	}

	@Override
	public ServiceComponent getComponent() {
		if (component == null) {
			throw new UnsupportedOperationException(PROXY_NO_SETTED);
		}
		return component;
	}

	@Override
	public RepositoryProxy getRepository() {
		return repositoryProxy;
	}

	@Override
	public void setRepository(RepositoryProxy repository) {

		this.repositoryProxy = repository;
	}
}