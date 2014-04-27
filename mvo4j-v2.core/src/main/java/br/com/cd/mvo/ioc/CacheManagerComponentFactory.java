package br.com.cd.mvo.ioc;

import br.com.cd.mvo.CacheManager;
import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.core.DefaultCacheManager;

public class CacheManagerComponentFactory extends AbstractComponentFactory<CacheManager> {

	private long cacheSize = -1;

	public CacheManagerComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected CacheManager getInstanceInternal() throws NoSuchBeanDefinitionException {

		try {
			return new DefaultCacheManager(getCacheSize());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	protected String getComponentBeanName() {
		return CacheManager.BEAN_NAME;
	}

	private long getCacheSize() throws ConfigurationException {

		if (cacheSize == -1)
			cacheSize = container.getContainerConfig().getInitParameter(ConfigParamKeys.CACHE_MANAGER_MAX_SIZE,
					ConfigParamKeys.DefaultValues.CACHE_MANAGER_MAX_SIZE);

		return this.cacheSize;
	}
}