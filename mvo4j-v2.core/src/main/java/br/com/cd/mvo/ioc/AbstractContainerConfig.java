package br.com.cd.mvo.ioc;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.util.ParserUtils;

public abstract class AbstractContainerConfig<D> implements ContainerConfig<D> {

	protected final D localContainer;

	private ContainerListener listener;

	public AbstractContainerConfig(D localContainer, ContainerListener listener) {
		this.localContainer = localContainer;
		this.listener = listener;
	}

	@Override
	public D getLocalContainer() {
		return localContainer;
	}

	@Override
	public ContainerListener getListener() {

		return listener;
	}

	public void setListener(ContainerListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean getInitBooleanParameter(String key) {

		return ParserUtils.parseBoolean(this.getInitParameter(key));
	}

	@Override
	public int getInitIntParameter(String key) {

		return ParserUtils.parseInt(this.getInitParameter(key));
	}

	@Override
	public <T> T getInitParameter(String key, T defaultValue) {

		return ParserUtils.parseObject(this.getInitParameter(key), defaultValue);
	}

	@Override
	public <T> T getInitParameter(String key, Class<T> resultType) {

		return ParserUtils.parseObject(resultType, this.getInitParameter(key));
	}

	@Override
	public <T> T getInitParameter(String key, Class<T> resultType, Object defaultValue) {

		return ParserUtils.parseObject(resultType, this.getInitParameter(key), defaultValue);
	}

	@Override
	public ContainerProvider<ContainerConfig<D>> getProvider() throws ConfigurationException {

		String className = this.getInitParameter(ConfigParamKeys.CDI_PROVIDER_CLASS, ConfigParamKeys.DefaultValues.CDI_PROVIDER_CLASS);

		Class<? extends ContainerProvider<?>> providerType = (className != null && className.isEmpty()) ? getContainerProvider(className) : this
				.getDefaultProvider();

		ContainerProvider<ContainerConfig<D>> provider = tryInstance(providerType);

		return provider;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ContainerProvider<?>> getContainerProvider(String className) throws ConfigurationException {

		try {
			return (Class<? extends ContainerProvider<?>>) Class.forName(className);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	private Class<? extends ContainerProvider<?>> getDefaultProvider() throws ConfigurationException {

		return getContainerProvider(ConfigParamKeys.DefaultValues.CDI_PROVIDER_CLASS);
	}

	@SuppressWarnings("unchecked")
	private ContainerProvider<ContainerConfig<D>> tryInstance(Class<? extends ContainerProvider<?>> providerType) throws ConfigurationException {

		try {
			return (ContainerProvider<ContainerConfig<D>>) providerType.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

}