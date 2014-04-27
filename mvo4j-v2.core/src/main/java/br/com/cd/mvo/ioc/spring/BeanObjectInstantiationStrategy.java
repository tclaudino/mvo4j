package br.com.cd.mvo.ioc.spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;

import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.HandleParameterAnnotationsMethodInvokeCallback;
import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Proxifier;
import br.com.cd.util.StringUtils;
import br.com.cd.util.ThreadLocalMapUtil;

public class BeanObjectInstantiationStrategy extends CglibSubclassingInstantiationStrategy {

	static Logger looger = Logger.getLogger(BeanObjectInstantiationStrategy.class);

	private final Container container;

	public BeanObjectInstantiationStrategy(Container container) {
		this.container = container;
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {

		Object instance;
		try {
			instance = proxify(beanDefinition, null, null);
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}

		instance = instance != null ? instance : super.instantiate(beanDefinition, beanName, owner);
		return cache(beanDefinition, instance);
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Constructor<?> ctor, Object[] args) {

		Object instance;
		try {
			instance = proxify(beanDefinition, ctor, args);
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}

		instance = instance != null ? instance : super.instantiate(beanDefinition, beanName, owner, ctor, args);
		return cache(beanDefinition, instance);
	}

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Object factoryBean,
			Method factoryMethod, Object[] args) {

		Object instance;
		try {
			instance = proxify(beanDefinition, null, args);
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}

		instance = instance != null ? instance : super.instantiate(beanDefinition, beanName, owner, factoryBean, factoryMethod, args);
		return cache(beanDefinition, instance);
	}

	private Object cache(RootBeanDefinition beanDefinition, Object obj) {

		ThreadLocalMapUtil.setThreadVariable(beanDefinition.getBeanClassName(), obj);
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object proxify(RootBeanDefinition beanDefinition, Constructor<?> ctor, Object[] args) throws ConfigurationException {

		looger.debug("");
		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("proxifying bean '{0}', class '{1}', constructor '{2}', args '{3}'",
				beanDefinition.getBeanClassName(), beanDefinition.getBeanClass(), ctor, Arrays.toString(args)));
		looger.debug(".................................................container.containsBean(beanMetaDataName)..............................");

		Object threadVariable = ThreadLocalMapUtil.getThreadVariable(beanDefinition.getBeanClassName());
		if (threadVariable != null && threadVariable.getClass().equals(beanDefinition.getBeanClass())) {
			looger.debug(StringUtils.format("found cached '{0}'. returning...", threadVariable));
			looger.debug("...............................................................................");
			return threadVariable;
		}

		if (!container.containsBean(beanDefinition.getBeanClassName())) {
			looger.debug("not found! skipping proxify...");
			looger.debug("...............................................................................");
			return null;
		}

		Object objBean;
		try {
			objBean = container.getBean(beanDefinition.getBeanClassName());
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
		if (!(objBean instanceof BeanMetaDataWrapper))
			return null;

		BeanMetaDataWrapper<?> metaDataWrapper = (BeanMetaDataWrapper<?>) objBean;

		BeanObject instance = null;
		String beanName = BeanMetaDataWrapper.generateBeanName(metaDataWrapper);
		threadVariable = ThreadLocalMapUtil.getThreadVariable(beanName);
		if (threadVariable != null) {
			looger.debug(StringUtils.format("found cached '{0}'", threadVariable));
			looger.debug("...............................................................................");

			if (threadVariable.getClass().equals(beanDefinition.getBeanClass()))
				return threadVariable;
			else if (threadVariable instanceof BeanObject)
				instance = (BeanObject) threadVariable;
		}

		br.com.cd.mvo.ioc.BeanFactory bf = container.getBean(BeanMetaDataWrapper.generateBeanFactoryName(metaDataWrapper),
				br.com.cd.mvo.ioc.BeanFactory.class);
		looger.debug(StringUtils.format("found! metaData '{0}', beanFactory '{1}'", metaDataWrapper, bf));
		if (instance == null) {

			looger.debug("getting wrapped instance...");
			instance = bf.getInstance(metaDataWrapper.getBeanMetaData());
		}

		Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME, Proxifier.class);

		// TODO: use cdi
		MethodInvokeCallback miCallback = bf.proxify(instance, metaDataWrapper.getBeanMetaData());
		miCallback = new HandleParameterAnnotationsMethodInvokeCallback(miCallback);

		looger.debug("creating proxy...");
		Object proxy;
		if (args == null || args.length == 0)
			proxy = proxifier.proxify(org.apache.commons.lang3.StringUtils.capitalize(metaDataWrapper.getBeanMetaData().name()),
					metaDataWrapper.getTargetBean(), instance, ctor, miCallback);
		else
			proxy = proxifier.proxify(org.apache.commons.lang3.StringUtils.capitalize(metaDataWrapper.getBeanMetaData().name()),
					metaDataWrapper.getTargetBean(), instance, ctor, miCallback, args);

		// to prevent circular bean creation in autowire dependency resolver.
		ThreadLocalMapUtil.setThreadVariable(beanDefinition.getBeanClassName(), proxy);
		ThreadLocalMapUtil.setThreadVariable(beanName, proxy);

		if (proxy instanceof BeanObject)
			bf.postConstruct((BeanObject) proxy, metaDataWrapper);

		looger.debug("...............................................................................");
		looger.debug(StringUtils.format("generated proxy bean '{0}' from beanName '{1}'", proxy, beanName));
		looger.debug("...............................................................................");

		return proxy;
	}
}