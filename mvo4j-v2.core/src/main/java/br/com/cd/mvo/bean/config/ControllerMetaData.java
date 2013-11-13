package br.com.cd.mvo.bean.config;

import br.com.cd.mvo.core.BeanObjectListener;

public class ControllerMetaData<T> extends DefaultBeanMetaData<T> {

	public static final String BEAN_NAME_SUFFIX = "Controller";

	@SuppressWarnings("unchecked")
	public ControllerMetaData(MetaData adaptee) {
		super(adaptee.get(TARGET_ENTITY, Class.class), adaptee);
	}

	@Override
	public String getBeanNameSuffix() {
		return ControllerMetaData.BEAN_NAME_SUFFIX;
	}

	// RelationMap[] relationMaps() default {};

	public static class ListenerMetaData<T> extends ControllerMetaData<T> {

		public ListenerMetaData(MetaData adaptee) {
			super(adaptee);
		}

		@Override
		public String getBeanNameSuffix() {
			return ControllerMetaData.BEAN_NAME_SUFFIX + BeanObjectListener.BEAN_NAME_SUFFIX;
			// return BeanObjectListener.BEAN_NAME_SUFFIX;
		}

	}
}