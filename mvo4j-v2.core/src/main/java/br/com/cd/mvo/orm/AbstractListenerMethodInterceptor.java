package br.com.cd.mvo.orm;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.BeanObjectListener;
import br.com.cd.mvo.core.Listenable;
import br.com.cd.mvo.ioc.AbstractMethodInvokeCallback;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.util.GenericsUtils;
import br.com.cd.util.StringUtils;
import br.com.cd.util.ThreadLocalMapUtil;

public class AbstractListenerMethodInterceptor<T, B extends BeanObject<T>, L extends BeanObjectListener<B>> extends
		AbstractMethodInvokeCallback {

	protected final Logger looger = Logger.getLogger(this.getClass());

	private static final String SELF_BEAN_ALREADY_WRAPPED = "SELF_BEAN_ALREADY_WRAPPED_";

	protected Collection<L> listeners = new LinkedHashSet<L>();

	protected B bean;
	protected Container container;

	public AbstractListenerMethodInterceptor(B bean, Container container) {

		this.bean = bean;
		this.container = container;
	}

	@Override
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		return super.beforeInvoke(target, method, invokeCallback, args);
	}

	@Override
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {

		if (method.isAnnotationPresent(PostConstruct.class))
			postConstruct((B) target);
		if (method.isAnnotationPresent(PreDestroy.class))
			preDestroy();

		switch (method.getName()) {
		// case "postConstruct":
		// this.postConstruct();
		default:
			super.afterInvoke(target, method, result, args);
		}
	}

	private void postConstruct(B target) throws ConfigurationException {

		this.configureListeners(target);

		for (L listener : this.listeners) {
			listener.postConstruct(target);
		}
	}

	private void preDestroy() {
		for (L listener : this.listeners) {
			listener.preDestroy(this.bean);
		}
	}

	@SuppressWarnings("unchecked")
	private void configureListeners(B targetBean) throws ConfigurationException {

		if (!(this.bean instanceof Listenable))
			return;

		looger.debug(StringUtils.format("postConstruct bean '{0}', adding listeners...", bean));

		String variableName = SELF_BEAN_ALREADY_WRAPPED + targetBean.getClass().getName();

		Object threadVariable = ThreadLocalMapUtil.getThreadVariable(variableName);
		looger.debug(StringUtils.format("cached bean '{0}' from key '{1}'. will be return if found", threadVariable, variableName));
		if (threadVariable != null)
			return;

		final Listenable<?> listenable = (Listenable<?>) bean;
		ThreadLocalMapUtil.setThreadVariable(variableName, listenable);

		// Class<?> listenableOf =
		// GenericsUtils.getTypeFor(listenable.getClass(), Listenable.class);
		Class<?> listenableOf = listenable.getListenerType();

		if (!container.containsBean(listenableOf.getName()))
			return;

		looger.debug("getting listeners.");
		Object obj = (Collection<?>) container.getBean(listenableOf.getName());
		if (!(obj instanceof Collection))
			return;

		Collection<Class<?>> listenerTypes = Collections.<Class<?>> emptyList();
		try {
			listenerTypes = (Collection<Class<?>>) obj;
		} catch (ClassCastException e) {
			looger.error(StringUtils.format("found listeners collections '{0}', but is not a Class Collections.", obj));
			throw new ConfigurationException(e);
		}
		looger.debug(StringUtils.format("found listeners '{0}'! add...", listenerTypes));

		looger.debug(StringUtils.format("process callback listener for bean '{0}'! adding...", targetBean));
		addListeners(targetBean, listenable, listenerTypes);
	}

	@SuppressWarnings("unchecked")
	private void addListeners(final BeanObject<?> targetBean, Listenable<?> listenable, Collection<Class<?>> listenerTypes) {

		// to prevent lookup himself bean listener in container
		if (BeanObjectListener.class.isAssignableFrom(targetBean.getClass())) {
			BeanObjectListener<?> listener = (BeanObjectListener<?>) targetBean;

			Class<?> listenerOf = getListenerInterface(listener.getClass());

			if (listenable.getListenerType().equals(listenerOf)) {

				looger.debug(StringUtils.format("this bean is a listener '{0}'...", listener));

				Class<?> targetEntity = GenericsUtils.getTypeFor(listener.getClass(),
						getListenerInterface((Class<? extends BeanObjectListener<?>>) listener.getClass()));
				if (!targetBean.getBeanMetaData().targetEntity().equals(targetEntity))
					looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
				else
					addListener(listenable, listener);
			}
		}

		for (Class<?> listenerType : listenerTypes) {

			Class<?> listenerOf = getListenerInterface((Class<? extends BeanObjectListener<?>>) listenerType);
			if (!listenable.getListenerType().equals(listenerOf))
				continue;

			if (listenerType.isAssignableFrom(targetBean.getClass()))
				continue;

			Class<?> targetEntity = GenericsUtils.getTypeFor(listenerType,
					getListenerInterface((Class<? extends BeanObjectListener<?>>) listenerType));
			if (!targetBean.getBeanMetaData().targetEntity().equals(targetEntity)) {
				looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
				continue;
			}

			BeanObjectListener<?> listener;
			Object threadVariable = ThreadLocalMapUtil.getThreadVariable(listenerType.getName());
			if (threadVariable != null && listenerType.isAssignableFrom(threadVariable.getClass()))
				listener = (BeanObjectListener<?>) threadVariable;
			else
				listener = container.getBean(listenerType.getName(), BeanObjectListener.class);

			addListener(listenable, listener);
		}
	}

	private void addListener(Listenable<?> listenable, BeanObjectListener<?> listener) {
		Class<?> listenerType = getListenerInterface(listener.getClass());
		// Class<?> listenableOf =
		// GenericsUtils.getTypeFor(listenable.getClass(), Listenable.class);
		Class<?> listenableOf = listenable.getListenerType();

		if (!listenerType.equals(listenableOf))
			looger.debug(StringUtils.format("this bean '{0}' is a listener of '{0}'. skip...", listenable, listenableOf));
		else {

			Class<?> targetEntity = GenericsUtils.getTypeFor(listener.getClass(), listenerType);

			if (((BeanObject<?>) listenable).getBeanMetaData().targetEntity().equals(targetEntity)) {
				looger.debug(StringUtils.format("this bean '{0}' is a listener himself. add...", listenable));
				addListener(listener);
			} else
				looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
		}
	}

	private Class<?> getListenerInterface(@SuppressWarnings("rawtypes") Class<? extends BeanObjectListener> listenerClass) {

		for (Class<?> ifc : listenerClass.getInterfaces())
			if (BeanObjectListener.class.isAssignableFrom(ifc))
				return ifc;

		return listenerClass;
	}

	@SuppressWarnings("unchecked")
	private void addListener(BeanObjectListener<?> listener) {
		if (!listeners.isEmpty())
			throw new IllegalArgumentException("Invalid duplication for bean '" + listener.getClass() + "' and targetEntity '"
					+ this.bean.getBeanMetaData().targetEntity() + "'");

		try {
			listeners.add((L) listener);
		} catch (ClassCastException e) {
			looger.error(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", this.bean
					.getBeanMetaData().targetEntity()), e);
		}
	}
}
