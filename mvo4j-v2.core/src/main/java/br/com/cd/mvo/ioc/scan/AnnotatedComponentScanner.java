package br.com.cd.mvo.ioc.scan;

import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void scan(Scanner scanner, Container container)
			throws ConfigurationException {

		for (BeanMetaDataFactory bmf : this.metaDataFactories) {

			Collection<Class<?>> beanTypes = bmf.scan(scanner, packagesToScan);

			for (Class<?> beanType : beanTypes) {

				// Annotation annotation = beanType.getAnnotation(bmf
				// .getBeanAnnotationType());

				WriteablePropertyMap propertyMap = bmf
						.newDefaultPropertyMap(container);

				// BeanMetaData beanMetaData = bmf.createBeanMetaData(
				// propertyMap, beanType, annotation);

				// BeanMetaDataWrapper<BeanMetaData> metaDataWrapper = new
				// BeanMetaDataWrapper<BeanMetaData>(
				// beanType, beanMetaData);
				BeanMetaDataWrapper<?> metaDataWrapper = bmf
						.createBeanMetaData(propertyMap, beanType, container);

				container.registerBean(metaDataWrapper);
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
