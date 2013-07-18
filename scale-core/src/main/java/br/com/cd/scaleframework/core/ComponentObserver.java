package br.com.cd.scaleframework.core;

public interface ComponentObserver<O extends ComponentObject<?>> {

	void onDiscoverTarget(O component);

	Class<O> getComponentType();

	Class<?> getTargetEntity();

}