package br.com.cd.mvo.ioc.scan;

import java.lang.annotation.Annotation;
import java.util.Collection;

import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.DefaultCrudController;
import br.com.cd.mvo.ioc.BeanFactory;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.Container;

public class AnnotatedComponentScanner extends AbstractComponentScanner {

	public AnnotatedComponentScanner(String... packageToScan) {
		super(packageToScan);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void scan(Scanner scanner, Container container)
			throws ConfigurationException {

		for (BeanMetaDataFactory metaDataFactory : this.metaDataFactories) {

			Collection<Class<?>> scan = scanner.scan(packageToScan,
					metaDataFactory.getBeanAnnotationType());

			for (Class<?> beanType : scan) {

				Annotation annotation = beanType.getAnnotation(metaDataFactory
						.getBeanAnnotationType());

				for (ComponentFactory<BeanFactory<?, ?>> dynamicBeanFactory : container
						.getComponentFactories()) {

					BeanFactory cf = dynamicBeanFactory.getInstance();

					if (!cf.isCandidate(annotation.getClass()))
						continue;

					BeanMetaData beanMetaData = metaDataFactory
							.createBeanMetaData(beanType, annotation);

					BeanMetaDataWrapper<BeanMetaData> metaDataWrapper = new BeanMetaDataWrapper<BeanMetaData>(
							DefaultCrudController.class, beanMetaData);

					container.registerBean(metaDataWrapper);
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
