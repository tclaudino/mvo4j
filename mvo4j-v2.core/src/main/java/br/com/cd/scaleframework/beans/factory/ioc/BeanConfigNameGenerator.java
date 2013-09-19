package br.com.cd.scaleframework.beans.factory.ioc;

public class BeanConfigNameGenerator {

	public String generateBeanName(Class<?> targetBean, Class<?> targetEntity) {

		return targetBean.getName() + "_" + targetEntity.getName();
	}

}
