package br.com.cd.scaleframework.beans.dynamic.factory.support;

import br.com.cd.scaleframework.beans.factory.ioc.ComponentFactoryContainer;
import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.support.DefaultCacheManager;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.NoSuchBeanDefinitionException;

public class CacheManagerDynamicBeanFactory extends
		AbstractDynamicBeanFactory<CacheManager> {

	private long cacheTime = -1;

	public CacheManagerDynamicBeanFactory(ComponentFactoryContainer container) {
		super(container);
	}

	@Override
	public Class<CacheManager> getObjectType() {
		return CacheManager.class;
	}

	@Override
	protected CacheManager getInstanceInternal()
			throws NoSuchBeanDefinitionException {

		try {
			return new DefaultCacheManager(getCacheTime());
		} catch (ConfigurationException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	private long getCacheTime() throws ConfigurationException {

		if (cacheTime == -1)
			cacheTime = getContainer().getDiscoveryFactory()
					.getCacheManagerMaxSize();

		return this.cacheTime;
	}
}