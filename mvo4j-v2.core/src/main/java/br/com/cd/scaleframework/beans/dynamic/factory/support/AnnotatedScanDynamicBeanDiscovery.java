package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanManager;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;
import br.com.cd.scaleframework.controller.dynamic.CrudControllerBeanConfig;
import br.com.cd.scaleframework.core.orm.dynamic.ServiceBeanConfig;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBeanConfig;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;

public class AnnotatedScanDynamicBeanDiscovery implements DynamicBeanDiscovery {

	private String[] packageToScan;

	public AnnotatedScanDynamicBeanDiscovery(String... packageToScan) {
		this.packageToScan = packageToScan;
	}

	@Override
	public Collection<DynamicBeanManager<?>> getCandidates() {

		Collection<DynamicBeanManager<?>> beans = new ArrayList<DynamicBeanManager<? extends BeanConfig<?, ?>>>();

		beans.addAll(this.getServiceCandidates());
		beans.addAll(this.getControllerCandidates());
		beans.addAll(this.getCrudControllerCandidates());
		beans.addAll(this.getWebControllerCandidates());
		beans.addAll(this.getWebCrudControllerCandidates());
		return beans;
	}

	private Collection<DynamicBeanManager<ServiceBeanConfig<?, ?>>> getServiceCandidates() {

		Collection<DynamicBeanManager<ServiceBeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<ServiceBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBeanManager<ControllerBeanConfig<?, ?>>> getControllerCandidates() {

		Collection<DynamicBeanManager<ControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<ControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBeanManager<CrudControllerBeanConfig<?, ?>>> getCrudControllerCandidates() {

		Collection<DynamicBeanManager<CrudControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<CrudControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBeanManager<WebControllerBeanConfig<?, ?>>> getWebControllerCandidates() {

		Collection<DynamicBeanManager<WebControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<WebControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBeanManager<WebCrudControllerBeanConfig<?, ?>>> getWebCrudControllerCandidates() {

		Collection<DynamicBeanManager<WebCrudControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBeanManager<WebCrudControllerBeanConfig<?, ?>>>();

		return beans;
	}

}
