package br.com.cd.mvo.ioc;

import java.util.Collection;
import java.util.TreeSet;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.scan.BeanMetaDataFactory;
import br.com.cd.mvo.util.ParserUtils;

public abstract class AbstractContainerConfig<D> implements ContainerConfig<D> {

	protected final D localContainer;

	protected Collection<BeanMetaDataFactory<?, ?>> factories = new TreeSet<>();

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
	public ContainerListener getContainerListener() {

		return listener;
	}

	public void setListener(ContainerListener listener) {
		this.listener = listener;
	}

	@Override
	public Collection<BeanMetaDataFactory<?, ?>> getBeanMetaDataFactories() {

		return factories;
	}

	@Override
	public boolean getInitBooleanParameter(String key) {

		return this.getInitBooleanParameter(key, false);
	}

	@Override
	public boolean getInitBooleanParameter(String key, boolean defaultValue) {

		return ParserUtils.parseBoolean(this.getInitParameter(key),
				defaultValue);
	}

	@Override
	public int getInitIntParameter(String key) {

		return this.getInitIntParameter(key, 0);
	}

	@Override
	public int getInitIntParameter(String key, int defaultValue) {

		return ParserUtils.parseInt(this.getInitParameter(key), defaultValue);
	}

	@Override
	public <T> T getInitParameter(String key, T defaultValue) {

		return ParserUtils
				.parseObject(this.getInitParameter(key), defaultValue);
	}

	@Override
	public <T> T getInitParameter(String key, Class<T> resultType) {

		return ParserUtils.parseObject(resultType, this.getInitParameter(key));
	}

	@Override
	public <T> T getInitParameter(String key, Class<T> resultType,
			T defaultValue) {

		return ParserUtils.parseObject(resultType, this.getInitParameter(key),
				defaultValue);
	}

	@Override
	public ContainerProvider<ContainerConfig<D>> getContainerProvider()
			throws ConfigurationException {

		String className = this.getInitParameter(
				ConfigParamKeys.PROVIDER_CLASS,
				ConfigParamKeys.DefaultValues.PROVIDER_CLASS);

		Class<? extends ContainerProvider<?>> providerType = (className != null && className
				.isEmpty()) ? getContainerProvider(className) : this
				.getDefaultProvider();

		ContainerProvider<ContainerConfig<D>> provider = tryInstance(providerType);

		return provider;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends ContainerProvider<?>> getContainerProvider(
			String className) throws ConfigurationException {

		try {
			return (Class<? extends ContainerProvider<?>>) Class
					.forName(className);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	private Class<? extends ContainerProvider<?>> getDefaultProvider()
			throws ConfigurationException {

		return getContainerProvider(ConfigParamKeys.DefaultValues.PROVIDER_CLASS);
	}

	@SuppressWarnings("unchecked")
	private ContainerProvider<ContainerConfig<D>> tryInstance(
			Class<? extends ContainerProvider<?>> providerType)
			throws ConfigurationException {

		try {
			return (ContainerProvider<ContainerConfig<D>>) providerType
					.newInstance();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

}