package br.com.cd.mvo.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.orm.Repository;

public class DefaultCrudService<T> implements CrudService<T>, Listenable<CrudServiceListener<T>> {

	private final Repository<T> repository;

	protected ServiceMetaData<T> metaData;

	private Collection<CrudServiceListener<T>> listeners = new LinkedHashSet<CrudServiceListener<T>>();

	public DefaultCrudService(Repository<T> repository, ServiceMetaData<T> metaData) {
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
	public Repository<T> getRepository() {
		return this.repository;
	}

	@PostConstruct
	@Override
	public void afterPropertiesSet() {
		for (CrudServiceListener<T> listener : this.getListeners()) {
			listener.postConstruct(this);
		}
	}

	@Override
	public BeanMetaData<T> getBeanMetaData() {
		return metaData;
	}

	@PreDestroy
	@Override
	public final void destroy() {
		for (CrudServiceListener<T> listener : this.getListeners()) {
			listener.preDestroy(this);
		}
	}

	@Override
	public Collection<CrudServiceListener<T>> getListeners() {
		return listeners;
	}

	@Override
	public void addListener(CrudServiceListener<T> listener) {

		this.listeners.add(listener);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends CrudServiceListener> getListenerType() {
		return CrudServiceListener.class;
	}

}
