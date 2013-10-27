package br.com.cd.mvo.ioc.scan;

import java.lang.reflect.Field;
import java.util.Collection;

import br.com.cd.mvo.bean.WriteablePropertyMap;
import br.com.cd.mvo.bean.config.BeanMetaData;
import br.com.cd.mvo.bean.config.helper.BeanMetaDataWrapper;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.Proxifier;

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
				.getPersistenceManagerFactory().getEntityAnnotation(),
				packagesToScan);

		Proxifier proxifier = container.getBean(Proxifier.BEAN_NAME,
				Proxifier.class);

		for (Class<?> entity : entities) {

			Class<?> entityId = null;
			for (Field field : entity.getDeclaredFields()) {
				if (field.isAnnotationPresent(container
						.getPersistenceManagerFactory()
						.getEntityIdentifierAnnotation())) {
					entityId = field.getType();
					break;
				}
			}
			if (entityId == null) {
				continue;
			}

			// for (ComponentFactory<BeanFactory<?, ?>> compFactory : container
			// .getComponentFactories()) {
			// BeanFactory<?, ?> bf = compFactory.getInstance();
			// BeanMetaDataFactory<?, ?> bmf = bf.getBeanMetaDataFactory();

			for (BeanMetaDataFactory<?, ?> bmf : this.metaDataFactories) {

				if (bmf.getBeanAnnotationType().equals(NoScan.class)) {
					continue;
				}

				WriteablePropertyMap propertyMap = bmf
						.newDefaultPropertyMap(container);

				propertyMap.add(BeanMetaData.TARGET_ENTITY, entity);
				propertyMap.add(BeanMetaData.ENTITY_ID_TYPE, entityId);

				String className = entity.getSimpleName()
						+ entity.getPackage().getName().hashCode()
						+ bmf.getBeanObjectType().getSimpleName();

				Class<?> proxyClass = proxifier.proxify(className,
						EmptyBeanObject.class);

				BeanMetaDataWrapper<?> metaDataWrapper = bmf
						.createBeanMetaData(propertyMap, proxyClass, container,
								false);

				String beanMetaDataName = BeanMetaDataWrapper
						.generateBeanMetaDataName(metaDataWrapper);

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

	public static class EmptyBeanObject implements BeanObject {

		@Override
		public BeanMetaData getBeanMetaData() {
			return null;
		}
	}

}
