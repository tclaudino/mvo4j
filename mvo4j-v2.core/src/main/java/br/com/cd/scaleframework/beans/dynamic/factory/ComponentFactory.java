package br.com.cd.scaleframework.beans.dynamic.factory;

public interface ComponentFactory {

	boolean isComponentCandidate(DynamicBean<?> beanConfig);

	Object getComponent(DynamicBean<?> beanConfig);

	String generateBeanName(DynamicBean<?> beanConfig);

	Class<?> createComponentProxy(DynamicBean<?> beanConfig);

}