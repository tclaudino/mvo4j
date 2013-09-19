package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;

public class EntityScanDynamicBeanDiscovery implements DynamicBeanDiscovery {

	private String packageToScan;

	public EntityScanDynamicBeanDiscovery(String packageToScan) {
		this.packageToScan = packageToScan;
	}

	@Override
	public Collection<DynamicBeanManager<? extends BeanConfig<?, ?>>> getCandidates() {

		Collection<DynamicBeanManager<? extends BeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<? extends BeanConfig<?, ?>>>();

		return beans;
	}

}
