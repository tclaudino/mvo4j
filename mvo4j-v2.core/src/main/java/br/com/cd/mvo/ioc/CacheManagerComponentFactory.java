package br.com.cd.mvo.ioc.support;

import br.com.cd.mvo.CacheManager;
import br.com.cd.mvo.DefaultCacheManager;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;

public class CacheManagerComponentFactory extends AbstractComponentFactory<CacheManager> {

	private long cacheTime = -1;

	public CacheManagerComponentFactory(Container container) {
		super(container);
	}

	@Override
	protected CacheManager getInstanceInternal() throws NoSuchBeanDefinitionException {

		try {
			return new DefaultCacheManager(getCacheTime());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	protected String getComponentBeanName() {
		return CacheManager.BEAN_NAME;
	}

	private long getCacheTime() throws ConfigurationException {

		if (cacheTime == -1) cacheTime = container.getApplicationConfig().getCacheManagerMaxSize();

		return this.cacheTime;
	}
}