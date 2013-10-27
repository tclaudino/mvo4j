package br.com.cd.mvo.core;

import java.util.List;

public interface ListenableController<T> extends Controller<T> {

	List<ControllerListener<T>> getListeners();

	void addListener(ControllerListener<T> listener);

	void afterPropertiesSet();

	void destroy();
}