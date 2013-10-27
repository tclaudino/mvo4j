package br.com.cd.mvo.bean.config;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.PropertyMap;
import br.com.cd.mvo.bean.ServiceBean;

public class ServiceMetaData extends BeanMetaData {

	public static final String BEAN_NAME_SUFFIX = "Service";

	public ServiceMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ServiceBean.class;
	}

	@Override
	public String getBeanNameSuffix() {
		return ServiceMetaData.BEAN_NAME_SUFFIX;
	}

	public static class ListenerMetaData extends ServiceMetaData {

		public static final String BEAN_NAME_SUFFIX = ServiceMetaData.BEAN_NAME_SUFFIX
				+ "Listener";

		public ListenerMetaData(PropertyMap adaptee) {
			super(adaptee);
		}

		@Override
		public String getBeanNameSuffix() {
			return ListenerMetaData.BEAN_NAME_SUFFIX;
		}
	}

}