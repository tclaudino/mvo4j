package br.com.cd.scaleframework.core.proxy;

import java.io.Serializable;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.orm.Repository;

@SuppressWarnings("rawtypes")
public interface RepositoryProxy<T> extends Repository<T, Serializable>,
		ComponentProxy<RepositoryComponent> {

	Repository<T, Serializable> getRepository();

	void setRepository(Repository<T, Serializable> repository);

}