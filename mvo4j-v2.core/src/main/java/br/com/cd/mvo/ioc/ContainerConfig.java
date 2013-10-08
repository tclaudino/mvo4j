package br.com.cd.mvo.ioc;

import java.util.Collection;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;

public interface ContainerConfig<D> {

	boolean getInitBooleanParameter(String key, boolean defaultValue);

	boolean getInitBooleanParameter(String key);

	int getInitIntParameter(String key, int defaultValue);

	int getInitIntParameter(String key);

	<T> T getInitParameter(String key, T defaultValue);

	<T> T getInitParameter(String key, Class<T> resultType);

	<T> T getInitParameter(String key, Class<T> resultType, T defaultValue);

	String getInitParameter(String key);

	D getLocalContainer();

	ContainerListener getContainerListener();

	Collection<BeanMetaDataFactory<?, ?>> getBeanMetaDataFactories();

	ContainerProvider<ContainerConfig<D>> getContainerProvider()
			throws ConfigurationException;

}
