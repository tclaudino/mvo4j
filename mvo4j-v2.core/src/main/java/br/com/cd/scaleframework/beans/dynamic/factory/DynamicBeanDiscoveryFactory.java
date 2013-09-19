package br.com.cd.scaleframework.beans.dynamic.factory;

import java.util.List;

import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public interface DynamicBeanFactory {

	boolean containsBean(String beanName);

	Object getBean(String beanName, Object... args)
			throws NoSuchBeanDefinitionException;

	<T> T getBean(String beanName, Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException, ClassCastException;

	<T> T getBean(Class<T> beanType, Object... args)
			throws NoSuchBeanDefinitionException;

	void addDiscovery(DynamicBeanDiscovery discovery);

	void setDiscoveries(List<DynamicBeanDiscovery> discoveries);

	void setDiscoveries(DynamicBeanDiscovery... discoveries);

	List<DynamicBeanDiscovery> getDiscoveries();

}
