package br.com.cd.mvo.bean.config.helper;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.StringUtils;

public class BeanMetaDataWrapper<D extends BeanMetaData> {

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

	public static String generateBeanMetaDataName(
			Class<? extends BeanMetaData> metaDataType, Class<?> targetEntity) {

		return metaDataType.getName() + "." + targetEntity.getName();
	}

	public static String generateBeanName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper) {

		return generateBeanName(metaDataWrapper, true);
	}

	public static String generateBeanName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper,
			boolean preferMetaDataName) {

		String beanName = metaDataWrapper.getBeanMetaData().name();

		return (beanName != null && !beanName.isEmpty() && preferMetaDataName) ? beanName
				: (StringUtils.cammelCase(metaDataWrapper.getBeanMetaData()
						.targetEntity().getSimpleName()) + metaDataWrapper
						.getBeanMetaData().getBeanNameSuffix());
	}

	public static String generateBeanMetaDataName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper) {

		return BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper,
				true);
	}

	public static String generateBeanMetaDataName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper,
			boolean preferMetaDataName) {

		return BeanMetaDataWrapper.generateBeanName(metaDataWrapper,
				!preferMetaDataName) + "Config";
	}

	private static Map<String, SoftReference<BeanMetaDataWrapper<?>>> cache = new HashMap<>();

	public static BeanMetaDataWrapper<?> getBeanMetaDataFromTargetEntity(
			Container container, Class<?> dependency, Class<?> targetEntity) {

		String key = dependency.getName() + "_" + targetEntity.getName();
		if (cache.containsKey(key)) {
			return cache.get(key).get();
		}

		@SuppressWarnings("rawtypes")
		Collection<BeanMetaDataWrapper> metaDatas = container
				.getBeansOfType(BeanMetaDataWrapper.class);

		for (ComponentFactory<BeanFactory<?, ?>> cf : container
				.getComponentFactories()) {

			BeanFactory<?, ?> bf;
			bf = cf.getInstance();
			for (BeanMetaDataWrapper<?> metaData : metaDatas) {
				if (bf.isCandidate(metaData.getBeanMetaData())
						&& dependency
								.isAssignableFrom(metaData.getTargetBean())
						&& targetEntity.isAssignableFrom(metaData
								.getBeanMetaData().targetEntity())) {

					cache.put(key, new SoftReference<BeanMetaDataWrapper<?>>(
							metaData));
					return metaData;
				}
			}
		}

		cache.put(key, null);
		return null;
	}

	public static BeanMetaDataWrapper<?> getBeanMetaData(Container container,
			Class<?> dependency, Class<?> declaringClass) {

		String key = dependency.getName() + "__" + declaringClass.getName();
		if (cache.containsKey(key)) {
			return cache.get(key).get();
		}

		@SuppressWarnings("rawtypes")
		Collection<BeanMetaDataWrapper> metaDatas = container
				.getBeansOfType(BeanMetaDataWrapper.class);

		BeanMetaDataWrapper<?> declaringClassMetaData = null;
		BeanMetaDataWrapper<?> dependecyMetaData = null;
		for (ComponentFactory<BeanFactory<?, ?>> cf : container
				.getComponentFactories()) {

			BeanFactory<?, ?> bf;
			bf = cf.getInstance();

			for (BeanMetaDataWrapper<?> metaData : metaDatas) {
				if (bf.isCandidate(metaData.getBeanMetaData())) {

					if (declaringClassMetaData == null
							&& metaData.getTargetBean().isAssignableFrom(
									declaringClass)) {
						declaringClassMetaData = metaData;
					}
					if (dependecyMetaData == null
							&& metaData.getTargetBean().isAssignableFrom(
									dependency)) {
						dependecyMetaData = metaData;
					}

					if ((declaringClassMetaData != null && dependecyMetaData != null)
							&& declaringClassMetaData
									.getBeanMetaData()
									.targetEntity()
									.equals(dependecyMetaData.getBeanMetaData()
											.targetEntity())) {

						cache.put(key,
								new SoftReference<BeanMetaDataWrapper<?>>(
										dependecyMetaData));
						return dependecyMetaData;
					}
				}
			}
		}

		cache.put(key, null);
		return null;
	}
}
