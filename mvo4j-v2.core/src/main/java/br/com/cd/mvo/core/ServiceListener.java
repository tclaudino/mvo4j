package br.com.cd.mvo.core;

public interface ServiceListener<T> {

	void postConstruct(CrudService<T> service);

	void preDestroy(CrudService<T> service);

}