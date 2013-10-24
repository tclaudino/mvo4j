package br.com.cd.mvo.ioc.scan;

import java.lang.reflect.Field;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.support.BeanFactoryUtils;

public class EntityComponentScanner extends AbstractComponentScanner {

	public EntityComponentScanner(String packageToScan,
			String... packagesToScan) {
		super(packageToScan, packagesToScan);
	}

	public EntityComponentScanner(String[] packagesToScan) {
		super(packagesToScan);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void scan(Scanner scanner, Container container)
			throws ConfigurationException {

		Collection<Class<?>> entities = scanner.scan(container
				.getPersistenceManagerFactory().getPersistenceTypeAnnotation(),
				packagesToScan);

		for (Class<?> entity : entities) {

			Class<?> entityId = null;
			for (Field field : entity.getDeclaredFields()) {
				if (field.isAnnotationPresent(container
						.getPersistenceManagerFactory()
						.getPersistenceIdentifierAnnotation())) {
					entityId = field.getType();
					break;
				}
			}
			if (entityId == null) {
				continue;
			}

			for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

				WriteablePropertyMap propertyMap = bmf
						.newDefaultPropertyMap(container);

				propertyMap.add(BeanMetaData.TARGET_ENTITY, entity);
				propertyMap.add(BeanMetaData.ENTITY_ID_TYPE, entityId);

				// BeanMetaData beanMetaData =
				// bmf.createBeanMetaData(propertyMap);
				//
				// @SuppressWarnings("rawtypes")
				// BeanMetaDataWrapper metaDataWrapper = new
				// BeanMetaDataWrapper<BeanMetaData>(
				// null, beanMetaData);
				BeanMetaDataWrapper<?> metaDataWrapper = bmf
						.createBeanMetaData(propertyMap);

				// propertyMap.add(BeanMetaData.NAME,
				// container.getBeanName(beanMetaDataWrapper));

				// propertyMap.add(BeanMetaData.NAME,
				// BeanFactoryUtils.generateBeanName(metaDataWrapper));

				String beanConfigName = BeanFactoryUtils
						.generateBeanMetaDataName(metaDataWrapper);

				if (!container.containsBean(beanConfigName)) {

					container.registerBean(metaDataWrapper);
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
