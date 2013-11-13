package br.com.cd.mvo.bean.config;

import br.com.cd.mvo.core.BeanObjectListener;

public class RepositoryMetaData<T> extends DefaultBeanMetaData<T> {

	public static final String BEAN_NAME_SUFFIX = "Repository";

	@SuppressWarnings("unchecked")
	public RepositoryMetaData(MetaData adaptee) {
		super(adaptee.get(TARGET_ENTITY, Class.class), adaptee);
	}

	@Override
	public String getBeanNameSuffix() {
		return RepositoryMetaData.BEAN_NAME_SUFFIX;
	}

	public static class ListenerMetaData<T> extends RepositoryMetaData<T> {

		public ListenerMetaData(MetaData adaptee) {
			super(adaptee);
		}

		@Override
		public String getBeanNameSuffix() {
			return RepositoryMetaData.BEAN_NAME_SUFFIX + BeanObjectListener.BEAN_NAME_SUFFIX;
			// return BeanObjectListener.BEAN_NAME_SUFFIX;
		}
	}

}