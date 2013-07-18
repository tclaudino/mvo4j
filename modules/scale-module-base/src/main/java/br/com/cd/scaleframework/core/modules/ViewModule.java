package br.com.cd.scaleframework.core.modules;

public interface ViewModule<T> {

	void doRequest(T view);
}
