package br.com.cd.scaleframework.core.proxy;

import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.orm.Service;

@SuppressWarnings("rawtypes")
public interface ServiceProxy extends Service, ComponentProxy<ServiceComponent> {

	RepositoryProxy getRepository();

	void setRepository(RepositoryProxy repository);

}