package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.orm.Repository;

public class RepositoryMetaDataFactory extends
		AbstractBeanMetaDataFactory<RepositoryMetaData, RepositoryBean> {

	public RepositoryMetaDataFactory() {
		super(Repository.class);
	}

	@Override
	public RepositoryMetaData doCreateBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE,
				ConfigParamKeys.DefaultValues.SCOPE_SINGLETON_NAME);
		RepositoryMetaData beanConfig = new RepositoryMetaData(propertyMap);

		return beanConfig;
	}
}
