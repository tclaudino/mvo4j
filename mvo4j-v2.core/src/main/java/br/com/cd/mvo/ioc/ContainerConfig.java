package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.ConfigurationException;

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

	ContainerProvider<ContainerConfig<D>> getContainerProvider() throws ConfigurationException;

}
