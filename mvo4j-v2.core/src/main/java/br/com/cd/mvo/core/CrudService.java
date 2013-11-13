package br.com.cd.mvo.core;

import java.io.Serializable;

import br.com.cd.mvo.orm.Repository;

public interface CrudService<T> extends BeanObject<T> {

	T save(final T entity);

	T update(T entity);

	void delete(final T entity);

	T find(final Serializable id);

	Repository<T> getRepository();

}
