package br.com.cd.mvo.ioc.scan;

import java.util.Collection;

import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public class AnnotatedComponentScanner extends AbstractComponentScanner {

	public AnnotatedComponentScanner(String packageToScan, String... packagesToScan) {
		super(packageToScan, packagesToScan);
	}

	public AnnotatedComponentScanner(String[] packagesToScan) {
		super(packagesToScan);
	}

	@Override
	public void scan(Scanner scanner, Container container) throws ConfigurationException {

		for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

			if (bmf.getBeanAnnotationType().equals(SubTypeScan.class))
				continue;

			Collection<Class<?>> beanTypes = bmf.scan(scanner, packagesToScan);

			for (Class<?> beanType : beanTypes) {

				if (beanType.isAnnotationPresent(SubTypeScan.class))
					continue;

				WriteableMetaData propertyMap = bmf.newDefaultPropertyMap(container.getContainerConfig());

				BeanMetaDataWrapper<?> metaDataWrapper = bmf.createBeanMetaData(propertyMap, beanType, true);

				BeanMetaDataWrapper<?> existentMetaData = BeanMetaDataWrapper.getBeanMetaData(container, bmf, metaDataWrapper.getBeanMetaData().targetEntity());
				if (existentMetaData != null)
					continue;

				container.registerBean(metaDataWrapper);
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
