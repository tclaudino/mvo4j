package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.util.StringUtils;

public class BeanFactoryUtils {

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

		return BeanFactoryUtils.generateBeanMetaDataName(metaDataWrapper, true);
	}

	public static String generateBeanMetaDataName(
			BeanMetaDataWrapper<? extends BeanMetaData> metaDataWrapper,
			boolean preferMetaDataName) {

		return BeanFactoryUtils.generateBeanName(metaDataWrapper,
				!preferMetaDataName) + "Config";
	}

}
