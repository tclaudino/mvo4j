package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.RepositoryBean;
import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.RepositoryMetaData;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.orm.Repository;

public class RepositoryMetaDataFactory extends AbstractBeanMetaDataFactory<RepositoryMetaData<?>, RepositoryBean> {

	public RepositoryMetaDataFactory() {
		super(Repository.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public RepositoryMetaData<?> doCreateBeanMetaData(WriteableMetaData propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE_NAME, ConfigParamKeys.DefaultValues.SCOPE_NAME_SINGLETON);
		RepositoryMetaData beanConfig = new RepositoryMetaData(propertyMap);

		return beanConfig;
	}
}
