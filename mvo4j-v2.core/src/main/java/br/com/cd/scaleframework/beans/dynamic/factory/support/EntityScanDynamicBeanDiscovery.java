package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;

public class EntityScanDynamicBeanDiscovery implements DynamicBeanDiscovery {

	private String[] packageToScan;

	public EntityScanDynamicBeanDiscovery(String... packageToScan) {
		this.packageToScan = packageToScan;
	}

	@Override
	public Collection<DynamicBeanManager<?>> getCandidates() {

		Collection<DynamicBeanManager<?>> beans = new ArrayList<DynamicBeanManager<?>>();

		return beans;
	}

}
