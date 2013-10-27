package br.com.cd.mvo.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.mvo.bean.config.ServiceMetaData;
import br.com.cd.mvo.orm.Repository;

public class DefaultCrudService<T> implements ListenableCrudService<T> {

	private final Repository<T> repository;

	protected ServiceMetaData metaData;

	private List<ServiceListener<T>> listeners = new LinkedList<ServiceListener<T>>();

	public DefaultCrudService(Repository<T> repository, ServiceMetaData metaData) {
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
		for (ServiceListener<T> listener : this.getListeners()) {
			listener.postConstruct(this);
		}
	}

	@Override
	public ServiceMetaData getBeanMetaData() {
		return metaData;
	}

	@PreDestroy
	@Override
	public final void destroy() {
		for (ServiceListener<T> listener : this.getListeners()) {
			listener.preDestroy(this);
		}
	}

	@Override
	public List<ServiceListener<T>> getListeners() {
		return listeners;
	}

	@Override
	public void addListener(ServiceListener<T> listener) {

		this.listeners.add(listener);
	}

}
