package br.com.cd.scaleframework.context.support;

public class DefaultCacheManager extends AbstractCacheManager {

	private int cacheTime;

	public DefaultCacheManager(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	@Override
	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

}
