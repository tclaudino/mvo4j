package br.com.cd.scaleframework.ioc;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public interface BeanRegistryFacade {

	BeanDefinitionRegistry getBeanDefinitionRegistry();

	void registerBean(Class<?> beanType);

	void overrideBean(Class<?> beanType, Class<?> overrideBeanType);

	void registerBean(String beanName, Class<?> beanType);

}
