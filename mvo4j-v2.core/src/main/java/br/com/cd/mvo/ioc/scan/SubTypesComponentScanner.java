package br.com.cd.mvo.ioc.scan;

import java.util.Collection;

import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public class SubTypesComponentScanner extends AbstractComponentScanner {

	public SubTypesComponentScanner(String packageToScan, String... packagesToScan) {
		super(packageToScan, packagesToScan);
	}

	public SubTypesComponentScanner(String[] packagesToScan) {
		super(packagesToScan);
	}

	@Override
	public void scan(Scanner scanner, Container container) throws ConfigurationException {

		for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

			if (!bmf.getBeanAnnotationType().equals(SubTypeScan.class))
				continue;

			Collection<Class<?>> beanTypes = bmf.scan(scanner, packagesToScan);

			for (Class<?> beanType : beanTypes) {

				if (!container.containsBean(beanType.getName()))
					container.registerBean(beanType.getName(), beanType);
			}

			container.registerSingleton(bmf.getBeanObjectType().getName(), beanTypes);
		}
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
