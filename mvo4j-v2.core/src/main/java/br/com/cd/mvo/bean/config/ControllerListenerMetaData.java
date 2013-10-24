package br.com.cd.mvo.bean.config;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.ControllerBean;
import br.com.cd.mvo.bean.PropertyMap;

public class ControllerListenerMetaData extends ControllerMetaData {

	public static final String BEAN_NAME_SUFFIX = "ControllerListener";

	public ControllerListenerMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ControllerBean.class;
	}

	@Override
	public String getBeanNameSuffix() {
		return ControllerListenerMetaData.BEAN_NAME_SUFFIX;
	}

}