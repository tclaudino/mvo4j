package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;

import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.Proxifier;

public class BeanObjectInstantiationStrategy extends
		CglibSubclassingInstantiationStrategy {

	private final Container container;

	public BeanObjectInstantiationStrategy(Container container) {
		this.container = container;
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition,
			String beanName, BeanFactory owner) {

		try {
			Object instantiate = instantiate(beanDefinition, null);
			if (instantiate != null)
				return instantiate;
		} catch (ConfigurationException e) {
		}
		return super.instantiate(beanDefinition, beanName, owner);
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition,
			String beanName, BeanFactory owner, Constructor<?> ctor,
			Object[] args) {

		try {
			Object instantiate = instantiate(beanDefinition, args);
			if (instantiate != null)
				return instantiate;
		} catch (ConfigurationException e) {
		}
		return super.instantiate(beanDefinition, beanName, owner, ctor, args);
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition,
			String beanName, BeanFactory owner, Object factoryBean,
			Method factoryMethod, Object[] args) {

		try {
			Object instantiate = instantiate(beanDefinition, args);
			if (instantiate != null)
				return instantiate;
		} catch (ConfigurationException e) {
		}

		return super.instantiate(beanDefinition, beanName, owner, factoryBean,
				factoryMethod, args);
	}

	interface Callback {

		Object instantiate();
	}

	@SuppressWarnings("unchecked")
	private Object instantiate(RootBeanDefinition beanDefinition, Object[] args)
			throws ConfigurationException {

		if (!container.containsBean(beanDefinition.getBeanClassName())) {
			// if (true) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		BeanMetaDataWrapper metaDataWrapper = container.getBean(
				beanDefinition.getBeanClassName(), BeanMetaDataWrapper.class);
		@SuppressWarnings("rawtypes")
		br.com.cd.mvo.ioc.BeanFactory bf = container.getBean(
				BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper),
				br.com.cd.mvo.ioc.BeanFactory.class);

		final BeanObject instance = bf.getInstance(metaDataWrapper);

		Proxifier proxyfier = container.getBean(Proxifier.BEAN_NAME,
				Proxifier.class);

		Object proxy;
		if (args == null || args.length == 0)
			proxy = proxyfier.proxify(StringUtils.capitalize(metaDataWrapper
					.getBeanMetaData().name()),
					metaDataWrapper.getTargetBean(), instance);
		else
			proxy = proxyfier.proxify(StringUtils.capitalize(metaDataWrapper
					.getBeanMetaData().name()),
					metaDataWrapper.getTargetBean(), instance, args);

		if (proxy instanceof BeanObject)
			proxy = bf.wrap((BeanObject) proxy);

		return proxy;
	}
}