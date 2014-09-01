package br.com.cd.mvo.ioc.scan;

import java.lang.reflect.Field;
import java.util.Collection;

import br.com.cd.mvo.core.BeanMetaData;
import br.com.cd.mvo.core.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DefaultBeanMetaData;
import br.com.cd.mvo.core.WriteableMetaData;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.NoProxy;
import br.com.cd.mvo.ioc.Proxifier;

public class EntityComponentScanner extends AbstractComponentScanner {

	public EntityComponentScanner(String packageToScan, String... packagesToScan) {
		super(packageToScan, packagesToScan);
	}

	public EntityComponentScanner(String[] packagesToScan) {
		super(packagesToScan);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void scan(Scanner scanner, Container container) throws ConfigurationException {

		Collection<Class<?>> entities = scanner.scan(container.getPersistenceManagerFactory().getEntityAnnotation(), packagesToScan);

		Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME, Proxifier.class);

		for (Class<?> targetEntity : entities) {

			Class<?> entityId = null;
			for (Field field : targetEntity.getDeclaredFields()) {
				if (field.isAnnotationPresent(container.getPersistenceManagerFactory().getEntityIdentifierAnnotation())) {
					entityId = field.getType();
					break;
				}
			}
			if (entityId == null) {
				continue;
			}

			for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

				if (bmf.getBeanAnnotationType().equals(SubTypeScan.class))
					continue;

				BeanMetaDataWrapper<?> existentMetaData = BeanMetaDataWrapper.getBeanMetaData(container, bmf, targetEntity);
				if (existentMetaData != null)
					continue;

				WriteableMetaData propertyMap = bmf.newDefaultPropertyMap(container.getContainerConfig());

				propertyMap.add(BeanMetaData.TARGET_ENTITY, targetEntity);
				propertyMap.add(BeanMetaData.ENTITY_ID_TYPE, entityId);

				String className = targetEntity.getSimpleName() + targetEntity.getPackage().getName().hashCode() + bmf.getBeanObjectType().getSimpleName();

				Class<? extends ScannedEntityBeanObject> proxyClass = proxifier.proxify(className, ScannedEntityBeanObject.class);

				BeanMetaDataWrapper<? extends DefaultBeanMetaData> metaDataWrapper = bmf.createBeanMetaData(propertyMap, proxyClass, false);

				String beanMetaDataName = BeanMetaDataWrapper.generateBeanMetaDataName(metaDataWrapper, false);

				if (!container.containsBean(beanMetaDataName)) {

					container.registerBean(metaDataWrapper);
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}

	public static class ScannedEntityBeanObject<T> implements BeanObject<T> {

		private BeanMetaData<T> metaData;

		public ScannedEntityBeanObject(BeanMetaData<T> metaData) {
			this.metaData = metaData;
		}

		@Override
		@NoProxy
		public BeanMetaData<T> getBeanMetaData() {
			return metaData;
		}
	}

}
