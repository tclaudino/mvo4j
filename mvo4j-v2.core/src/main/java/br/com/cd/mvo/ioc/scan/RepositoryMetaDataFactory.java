package br.com.cd.mvo.ioc.scan;

import javassist.NotFoundException;
import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.RepositoryMetaData;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.util.ProxyUtils;

public class RepositoryMetaDataFactory extends
		AbstractBeanMetaDataFactory<RepositoryMetaData, RepositoryBean> {

	public RepositoryMetaDataFactory() {
		super(Repository.class);
	}

	@Override
	public RepositoryMetaData createBeanMetaData(
			WriteablePropertyMap propertyMap) {

		propertyMap.add(BeanMetaData.SCOPE, "singleton");
		RepositoryMetaData beanConfig = new RepositoryMetaData(propertyMap);

		return beanConfig;
	}

	@Override
	public RepositoryMetaData createBeanMetaData(Class<?> beanType,
			RepositoryBean annotation) throws ConfigurationException {

		WriteablePropertyMap map = new WriteablePropertyMap();

		try {
			map.addAll(ProxyUtils.getAnnotationAtributes(beanType, annotation));
		} catch (NotFoundException e) {
			throw new ConfigurationException(e);
		}

		return createBeanMetaData(map);
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
