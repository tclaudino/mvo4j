package br.com.cd.mvo.core;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;

@SuppressWarnings("rawtypes")
public class BeanMetaDataWrapper<D extends DefaultBeanMetaData> {

	public static final String BEAN_METADATA_SUFFIX = "MetaData";
	public static final String BEAN_FACTORY_SUFFIX = "Factory";

	private final Class<?> targetBean;
	private final D metaData;

	public BeanMetaDataWrapper(Class<?> targetBean, D metaData) {
		this.targetBean = targetBean;
		this.metaData = metaData;
	}

	public Class<?> getTargetBean() {
		return targetBean;
	}

	public D getBeanMetaData() {
		return metaData;
	}

	public static String generateBeanMetaDataName(Class<?> metaDataType, Class<?> targetEntity) {

		return metaDataType.getName() + "." + targetEntity.getName();
	}

	public static String generateBeanName(BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper) {

		return generateBeanName(metaDataWrapper, true);
	}

	public static String generateBeanName(BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper, boolean preferMetaDataName) {

		String beanName = metaDataWrapper.getBeanMetaData().name();

		return (beanName != null && !beanName.isEmpty() && preferMetaDataName) ? beanName : (metaDataWrapper.getBeanMetaData().targetEntity().getSimpleName())
				+ metaDataWrapper.getBeanMetaData().getBeanNameSuffix();
	}

	public static String generateBeanFactoryName(BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper) {

		return BeanMetaDataWrapper.generateBeanName(metaDataWrapper, true) + BEAN_FACTORY_SUFFIX;
	}

	public static String generateBeanMetaDataName(BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper) {

		return BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper, true);
	}

	public static String generateBeanMetaDataName(BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper, boolean preferMetaDataName) {

		return BeanMetaDataWrapper.generateBeanName(metaDataWrapper, preferMetaDataName) + BEAN_METADATA_SUFFIX;
	}

	private static Map<String, SoftReference<BeanMetaDataWrapper>> cache = new HashMap<>();

	public static BeanMetaDataWrapper<?> getBeanMetaDataFromTargetEntity(Container container, Class<?> declaringClass, Class<?> dependency,
			Class<?> targetEntity) {

		String key = dependency.getName() + "_" + targetEntity.getName();
		if (cache.containsKey(key)) {
			return cache.get(key).get();
		}

		Collection<BeanMetaDataWrapper> metaDatas = container.getBeansOfType(BeanMetaDataWrapper.class);

		for (ComponentFactory<BeanFactory<?, ?>> cf : container.getComponentFactories()) {

			BeanFactory<?, ?> bf;
			bf = cf.getInstance();

			for (BeanMetaDataWrapper<?> metaData : metaDatas) {
				if (bf.isCandidate(metaData.getBeanMetaData())
						&& targetEntity.isAssignableFrom(metaData.getBeanMetaData().targetEntity())
						&&

						(metaData.getTargetBean().equals(declaringClass) || dependency.isAssignableFrom(bf.getBeanMetaDataFactory().getBeanObjectType()) || dependency
								.isAssignableFrom(metaData.getTargetBean()))) {

					cache.put(key, new SoftReference<BeanMetaDataWrapper>(metaData));
					return metaData;
				}
			}
		}

		cache.put(key, null);
		return null;
	}

	public static BeanMetaDataWrapper<?> getBeanMetaData(Container container, BeanMetaDataFactory<?, ?> bmf, Class<?> targetEntity) {

		Collection<BeanMetaDataWrapper> metaDatas = container.getBeansOfType(BeanMetaDataWrapper.class);

		for (ComponentFactory<BeanFactory<?, ?>> cf : container.getComponentFactories()) {

			BeanFactory<?, ?> bf;
			bf = cf.getInstance();

			for (BeanMetaDataWrapper<?> metaData : metaDatas) {
				if (bf.isCandidate(metaData.getBeanMetaData()) && targetEntity.isAssignableFrom(metaData.getBeanMetaData().targetEntity())
						&& bmf.getBeanObjectType().isAssignableFrom(bf.getBeanMetaDataFactory().getBeanObjectType())
						&& bmf.getBeanMetaDataType().isAssignableFrom(bf.getBeanMetaDataFactory().getBeanMetaDataType())) {

					return metaData;
				}
			}
		}

		return null;
	}

	public static BeanMetaDataWrapper<?> getBeanMetaData(Container container, Class<?> dependency, Class<?> declaringClass) {

		String key = dependency.getName() + "__" + declaringClass.getName();
		if (cache.containsKey(key)) {
			return cache.get(key).get();
		}

		Collection<BeanMetaDataWrapper> metaDatas = container.getBeansOfType(BeanMetaDataWrapper.class);

		BeanMetaDataWrapper<?> declaringClassMetaData = null;
		BeanMetaDataWrapper<?> dependecyMetaData = null;
		for (ComponentFactory<BeanFactory<?, ?>> cf : container.getComponentFactories()) {

			BeanFactory<?, ?> bf;
			bf = cf.getInstance();

			for (BeanMetaDataWrapper<?> metaData : metaDatas) {
				if (bf.isCandidate(metaData.getBeanMetaData())) {

					if (declaringClassMetaData == null && metaData.getTargetBean().isAssignableFrom(declaringClass)) {
						declaringClassMetaData = metaData;
					}
					if (dependecyMetaData == null && metaData.getTargetBean().isAssignableFrom(dependency)) {
						dependecyMetaData = metaData;
					}

					if ((declaringClassMetaData != null && dependecyMetaData != null)
							&& declaringClassMetaData.getBeanMetaData().targetEntity().equals(dependecyMetaData.getBeanMetaData().targetEntity())) {

						cache.put(key, new SoftReference<BeanMetaDataWrapper>(dependecyMetaData));
						return dependecyMetaData;
					}
				}
			}
		}

		cache.put(key, new SoftReference<BeanMetaDataWrapper>(null));
		return null;
	}

	@Override
	public String toString() {
		return "BeanMetaDataWrapper [targetBean=" + targetBean + ", metaData=" + metaData + "]";
	}

}
