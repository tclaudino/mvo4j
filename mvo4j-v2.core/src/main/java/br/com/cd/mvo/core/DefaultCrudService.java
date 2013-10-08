package br.com.cd.mvo.core;

import java.io.Serializable;

import br.com.cd.mvo.orm.Repository;

public class DefaultCrudService<T> implements CrudService<T> {

	private final Repository<T> repository;

	public DefaultCrudService(Repository<T> repository) {
		this.repository = repository;
	}

	@Override
	public T save(T entity) {

		return repository.save(entity);
	}

	@Override
	public T update(T entity) {

		return repository.update(entity);
	}

	@Override
	public void delete(T entity) {

		repository.delete(entity);
	}

	@Override
	public T find(Serializable id) {

		return repository.find(id);
	}

	@Override
	public Repository<T> getRepository() {
		return this.repository;
	}

}
