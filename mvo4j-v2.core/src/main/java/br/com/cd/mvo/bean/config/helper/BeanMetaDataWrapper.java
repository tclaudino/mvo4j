package br.com.cd.mvo.bean.config;

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

}
