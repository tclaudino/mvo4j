package br.com.cd.mvo.bean.config;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.PropertyMap;

public class ControllerMetaData extends BeanMetaData {

	public static final String BEAN_NAME_SUFFIX = "Controller";

	public ControllerMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ControllerBean.class;
	}

	@Override
	public String getBeanNameSuffix() {
		return ControllerMetaData.BEAN_NAME_SUFFIX;
	}
	// RelationMap[] relationMaps() default {};

}