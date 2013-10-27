package br.com.cd.mvo.core;

import br.com.cd.mvo.orm.Repository;

public interface RepositoryListener<T> extends Repository.ActionListener<T> {

	void postConstruct(Repository<T> repository);

	void preDestroy(Repository<T> repository);

}