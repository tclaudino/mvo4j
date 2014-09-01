package br.com.cd.mvo.core;


public class ServiceMetaData<T> extends DefaultBeanMetaData<T> {

	public static final String BEAN_NAME_SUFFIX = "Service";

	@SuppressWarnings("unchecked")
	public ServiceMetaData(MetaData adaptee) {
		super(adaptee.get(TARGET_ENTITY, Class.class), adaptee);
	}

	@Override
	public String getBeanNameSuffix() {
		return ServiceMetaData.BEAN_NAME_SUFFIX;
	}

	public static class ListenerMetaData<T> extends ServiceMetaData<T> {

		public ListenerMetaData(MetaData adaptee) {
			super(adaptee);
		}

		@Override
		public String getBeanNameSuffix() {
			return ServiceMetaData.BEAN_NAME_SUFFIX + BeanObjectListener.BEAN_NAME_SUFFIX;
			// return BeanObjectListener.BEAN_NAME_SUFFIX;
		}

	}

}