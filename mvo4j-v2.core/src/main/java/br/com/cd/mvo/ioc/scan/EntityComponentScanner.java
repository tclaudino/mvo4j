package br.com.cd.mvo.ioc.scan;

import java.lang.reflect.Field;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.BeanMetaDataWrapper;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public class EntityComponentScanner extends AbstractComponentScanner {

	public EntityComponentScanner(String... packageToScan) {
		super(packageToScan);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void scan(Scanner scanner, Container container)
			throws ConfigurationException {

		Collection<Class<?>> entities = scanner.scan(packageToScan, container
				.getPersistenceManagerFactory().getPersistenceTypeAnnotation());

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

			for (BeanMetaDataFactory<?, ?> beanMetaDataFactory : this.metaDataFactories) {

				String beanConfigName = container.getBeanMetaDataName(
						beanMetaDataFactory.getBeanType(), entity);
				if (!container.containsBean(beanConfigName)) {

					WriteablePropertyMap propertyMap = new WriteablePropertyMap();
					propertyMap.add(BeanMetaData.TARGET_ENTITY, entity);
					propertyMap.add(BeanMetaData.ENTITY_ID_TYPE, entityId);
					propertyMap.add(BeanMetaData.INITIAL_PAGE_SIZE, container
							.getInitApplicationConfig().getInitialPageSize());
					propertyMap.add(BeanMetaData.MESSAGE_BUNDLE, container
							.getInitApplicationConfig().getBundleName());
					propertyMap.add(BeanMetaData.SCOPE, container
							.getInitApplicationConfig().getScopeName());
					propertyMap.add(BeanMetaData.PERSISTENCE_FACTORY_QUALIFIER,
							container.getInitApplicationConfig()
									.getPersistenceFactoryQualifier());
					propertyMap.add(BeanMetaData.PERSISTENCE_PROVIDER,
							container.getInitApplicationConfig()
									.getPersistenceManagerFactoryClass());

					BeanMetaData beanMetaData = beanMetaDataFactory
							.createBeanMetaData(propertyMap);

					@SuppressWarnings("rawtypes")
					BeanMetaDataWrapper beanMetaDataWrapper = new BeanMetaDataWrapper<BeanMetaData>(
							beanMetaDataFactory.getBeanType(), beanMetaData);

					propertyMap.add(BeanMetaData.NAME,
							container.getBeanName(beanMetaDataWrapper));

					container.registerBean(beanMetaDataWrapper);
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}

}
