package br.com.cd.mvo.bean.config;

import java.lang.annotation.Annotation;

import br.com.cd.mvo.bean.PropertyMap;
import br.com.cd.mvo.bean.RepositoryBean;

public class RepositoryMetaData extends BeanMetaData {

	public static final String BEAN_NAME_SUFFIX = "Repository";

	public RepositoryMetaData(PropertyMap adaptee) {
		super(adaptee);
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return RepositoryBean.class;
	}

	@Override
	public String getBeanNameSuffix() {
		return RepositoryMetaData.BEAN_NAME_SUFFIX;
	}

	public static class ListenerMetaData extends RepositoryMetaData {

		public static final String BEAN_NAME_SUFFIX = RepositoryMetaData.BEAN_NAME_SUFFIX
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