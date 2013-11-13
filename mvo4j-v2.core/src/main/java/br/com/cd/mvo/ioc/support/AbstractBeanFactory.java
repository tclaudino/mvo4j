package br.com.cd.mvo.ioc.support;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.DefaultBeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.BeanObjectListener;
import br.com.cd.mvo.core.Listenable;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.mvo.util.GenericsUtils;
import br.com.cd.mvo.util.StringUtils;
import br.com.cd.mvo.util.ThreadLocalMapUtil;

@SuppressWarnings("rawtypes")
public abstract class AbstractBeanFactory<D extends DefaultBeanMetaData, A extends Annotation> implements BeanFactory<D, A> {

	protected Logger looger = Logger.getLogger(this.getClass());

	private static final String SELF_BEAN_ALREADY_WRAPPED = "SELF_BEAN_ALREADY_WRAPPED_";
	protected final Container container;
	protected final Class<D> metaDataType;
	protected final Class<A> annotationType;
	protected final BeanMetaDataFactory<D, A> metaDataFactory;

	@SuppressWarnings({ "unchecked" })
	public AbstractBeanFactory(Container container, BeanMetaDataFactory<D, A> metaDataFactory) {
		assert container != null : "'container' must be not null";
		assert metaDataFactory != null : "'metaDataFactory' must be not null";
		this.container = container;
		this.metaDataFactory = metaDataFactory;

		List<Class> typesFor = GenericsUtils.getTypesFor(this.getClass());
		this.metaDataType = typesFor.get(0);
		this.annotationType = typesFor.get(1);
		assert this.metaDataType != null : "generic parameter[0] <D extends BeanMetaData> must be not null";
		assert this.annotationType != null : "generic parameter[0] <A extends Annotation> must be not null";
	}

	@Override
	public boolean isCandidate(BeanMetaData metaData) {
		return metaDataType.equals(metaData.getClass());
	}

	@Override
	public boolean isCandidate(Class<? extends Annotation> annotation) {
		return this.annotationType.equals(annotation);
	}

	@Override
	public BeanMetaDataFactory<D, A> getBeanMetaDataFactory() {

		return metaDataFactory;
	}

	@Override
	public Class<D> getBeanMetaDataType() {
		return metaDataType;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void postConstruct(final BeanObject<?> bean, BeanMetaDataWrapper<?> metaDataWrapper) {

		if (!(bean instanceof Listenable)) return;

		looger.debug(StringUtils.format("postConstruct bean '{0}', adding listeners...", bean));

		String variableName = SELF_BEAN_ALREADY_WRAPPED + metaDataWrapper.getTargetBean().getName();

		Object threadVariable = ThreadLocalMapUtil.getThreadVariable(variableName);
		looger.debug(StringUtils.format("cached bean '{0}' from key '{1}'. will be return if found", threadVariable, variableName));
		if (threadVariable != null) return;

		final Listenable<BeanObjectListener<?>> listenable = (Listenable<BeanObjectListener<?>>) bean;
		ThreadLocalMapUtil.setThreadVariable(variableName, listenable);

		// Class<?> listenableOf =
		// GenericsUtils.getTypeFor(listenable.getClass(), Listenable.class);
		Class<?> listenableOf = listenable.getListenerType();

		if (!container.containsBean(listenableOf.getName())) return;

		looger.debug("getting listeners.");
		Object obj = (Collection<Class<?>>) container.getBean(listenableOf.getName());
		if (!(obj instanceof Collection)) return;
		Collection<Class<?>> listenerTypes = (Collection<Class<?>>) obj;
		looger.debug(StringUtils.format("found listeners '{0}'! add...", listenerTypes));

		looger.debug(StringUtils.format("process callback listener for bean '{0}'! adding...", bean));
		addListeners(bean, listenable, listenerTypes);
	}

	@SuppressWarnings("unchecked")
	private <T> void addListeners(final BeanObject<?> bean, Listenable<BeanObjectListener<?>> listenable, Collection<Class<?>> listenerTypes) {

		// to prevent lookup himself bean listener in container
		if (listenable instanceof BeanObjectListener) {
			BeanObjectListener listener = (BeanObjectListener) listenable;

			Class<?> listenerOf = getListenerInterface(listener.getClass());

			if (listenable.getListenerType().equals(listenerOf)) {

				looger.debug(StringUtils.format("this bean is a listener '{0}'...", listener));

				Class<?> targetEntity = GenericsUtils.getTypeFor(listener.getClass(),
						getListenerInterface((Class<? extends BeanObjectListener>) listener.getClass()));
				if (!bean.getBeanMetaData().targetEntity().equals(targetEntity))
					looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
				else
					addListener(listenable, listener);

			}
		}

		for (Class<?> listenerType : listenerTypes) {

			Class<?> listenerOf = getListenerInterface((Class<? extends BeanObjectListener>) listenerType);
			if (!listenable.getListenerType().equals(listenerOf)) continue;

			if (listenerType.isAssignableFrom(listenable.getClass())) continue;

			Class<?> targetEntity = GenericsUtils.getTypeFor(listenerType,
					getListenerInterface((Class<? extends BeanObjectListener>) listenerType));
			if (!bean.getBeanMetaData().targetEntity().equals(targetEntity)) {
				looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
				continue;
			}

			BeanObjectListener listener;
			Object threadVariable = ThreadLocalMapUtil.getThreadVariable(listenerType.getName());
			if (threadVariable != null && threadVariable.getClass().equals(listenerType))
				listener = (BeanObjectListener) threadVariable;
			else
				listener = container.getBean(listenerType.getName(), BeanObjectListener.class);

			addListener(listenable, listener);
		}
		listenable.afterPropertiesSet();
	}

	private void addListener(Listenable<BeanObjectListener<?>> listenable, BeanObjectListener listener) {
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
				listenable.addListener(listener);
			} else
				looger.debug(StringUtils.format("this bean '{0}' is a listener for another targetEntity '{0}'. skip...", targetEntity));
		}
	}

	private Class<?> getListenerInterface(Class<? extends BeanObjectListener> listenerClass) {

		for (Class<?> ifc : listenerClass.getInterfaces())
			if (BeanObjectListener.class.isAssignableFrom(ifc)) return ifc;

		return listenerClass;
	}

	@Override
	public String toString() {
		return "BeanFactory [metaDataType=" + metaDataType + ", annotationType=" + annotationType + ", metaDataFactory=" + metaDataFactory
				+ "]";
	}

}