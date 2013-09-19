package br.com.cd.scaleframework.beans.dynamic.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.cd.scaleframework.beans.dynamic.factory.support.AnnotatedScanDynamicBeanDiscovery;
import br.com.cd.scaleframework.beans.dynamic.factory.support.EntityScanDynamicBeanDiscovery;
import br.com.cd.scaleframework.context.ConfigParamKeys;

public class DynamicBeanDiscoveryFactory {

	private List<DynamicBeanDiscovery> discoveries = Collections.emptyList();
	private String[] packagesToScan;
	private String defaultLocale = ConfigParamKeys.DefaultValues.DEFAULT_LOCALE;
	private String[] suportedLocales = ConfigParamKeys.DefaultValues.SUPORTED_LOCALES
			.split(",");
	private long cacheManagerMaxSize = ConfigParamKeys.DefaultValues.CACHE_MANAGER_MAX_SIZE;
	private long i18nCacheTime = ConfigParamKeys.DefaultValues.I18N_CACHE_TIME;
	private String bundleName = ConfigParamKeys.DefaultValues.BUNDLE_NAME;

	public DynamicBeanDiscoveryFactory(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(Collection<String> packagesToScan) {
		this.setPackagesToScan(packagesToScan.toArray(new String[packagesToScan
				.size()]));
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String[] getSuportedLocales() {
		return suportedLocales;
	}

	public void setSuportedLocales(Collection<String> suportedLocales) {
		this.setSuportedLocales(suportedLocales
				.toArray(new String[suportedLocales.size()]));
	}

	public void setSuportedLocales(String... suportedLocales) {
		this.suportedLocales = suportedLocales;
	}

	public long getCacheManagerMaxSize() {
		return cacheManagerMaxSize;
	}

	public void setCacheManagerMaxSize(long cacheTime) {
		this.cacheManagerMaxSize = cacheTime;
	}

	public long getI18nCacheTime() {
		return i18nCacheTime;
	}

	public void setI18nCacheTime(long i18nCacheTime) {
		this.i18nCacheTime = i18nCacheTime;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public void addDiscovery(DynamicBeanDiscovery discovery) {
		this.discoveries.add(discovery);
	}

	public List<DynamicBeanDiscovery> getDiscoveries() {

		if (discoveries.isEmpty()) {
			discoveries.add(new AnnotatedScanDynamicBeanDiscovery(
					packagesToScan));
			discoveries.add(new EntityScanDynamicBeanDiscovery(packagesToScan));
		}

		return discoveries;
	}

	public void setDiscoveries(DynamicBeanDiscovery... discoveries) {
		this.setDiscoveries(Arrays.asList(discoveries));
	}

	public void setDiscoveries(List<DynamicBeanDiscovery> discoveries) {
		this.discoveries = discoveries;
	}

}