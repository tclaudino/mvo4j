package br.com.cd.mvo.core;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.CrudServiceListener;
import br.com.cd.mvo.orm.Repository;

@SuppressWarnings("rawtypes")
public class DefaultCrudService<T> implements CrudService<T>, Listenable<CrudServiceListener> {

	private final Repository<T, ?> repository;

	protected ServiceMetaData<T> metaData;

	public DefaultCrudService(Repository<T, ?> repository, ServiceMetaData<T> metaData) {
		this.repository = repository;
		this.metaData = metaData;
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
	public Repository<T, ?> getRepository() {
		return this.repository;
	}

	@PostConstruct
	@Override
	public void postConstruct() {
		// only proxy listener
	}

	@PreDestroy
	@Override
	public final void preDestroy() {
		// only proxy listener
	}

	@Override
	public Class<CrudServiceListener> getListenerType() {
		return CrudServiceListener.class;
	}

	@Override
	public BeanMetaData<T> getBeanMetaData() {
		return metaData;
	}

}
