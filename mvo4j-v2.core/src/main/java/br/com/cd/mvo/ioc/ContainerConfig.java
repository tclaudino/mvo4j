package br.com.cd.mvo.ioc;


public interface ContainerConfig<D> {

	boolean getInitBooleanParameter(String key);

	int getInitIntParameter(String key);

	String getInitParameter(String key);

	<T> T getInitParameter(String key, T defaultValue);

	<T> T getInitParameter(String key, Class<T> resultType);

	<T> T getInitParameter(String key, Class<T> resultType, Object defaultValue);

	D getLocalContainer();

	ContainerListener getListener();

	ContainerProvider<ContainerConfig<D>> getProvider() throws ConfigurationException;

}
