package br.com.cd.mvo.core;

import java.util.List;

public interface ListenableCrudService<T> extends CrudService<T> {

	List<ServiceListener<T>> getListeners();

	void addListener(ServiceListener<T> listener);

	void afterPropertiesSet();

	void destroy();
}
