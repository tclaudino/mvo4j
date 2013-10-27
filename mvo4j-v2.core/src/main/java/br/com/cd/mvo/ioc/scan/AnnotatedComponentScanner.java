package br.com.cd.mvo.ioc.scan;

import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public class AnnotatedComponentScanner extends AbstractComponentScanner {

	public AnnotatedComponentScanner(String packageToScan,
			String... packagesToScan) {
		super(packageToScan, packagesToScan);
	}

	public AnnotatedComponentScanner(String[] packagesToScan) {
		super(packagesToScan);
	}

	@Override
	public void scan(Scanner scanner, Container container)
			throws ConfigurationException {

		// for (ComponentFactory<BeanFactory<?, ?>> compFactory : container
		// .getComponentFactories()) {
		// BeanFactory<?, ?> bf = compFactory.getInstance();
		// BeanMetaDataFactory<?, ?> bmf = bf.getBeanMetaDataFactory();

		for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

			Collection<Class<?>> beanTypes = bmf.scan(scanner, packagesToScan);

			for (Class<?> beanType : beanTypes) {

				if (beanType.isAnnotationPresent(NoScan.class))
					continue;

				WriteablePropertyMap propertyMap = bmf
						.newDefaultPropertyMap(container);

				BeanMetaDataWrapper<?> metaDataWrapper = bmf
						.createBeanMetaData(propertyMap, beanType, container,
								true);

				container.registerBean(metaDataWrapper);
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
