package br.com.cd.mvo.ioc;


@SuppressWarnings("rawtypes")
public interface ComponentFactory<T> extends Comparable<ComponentFactory> {

	T getInstance() throws NoSuchBeanDefinitionException;

	Class<T> getComponentType();

	int getOrder();

}
