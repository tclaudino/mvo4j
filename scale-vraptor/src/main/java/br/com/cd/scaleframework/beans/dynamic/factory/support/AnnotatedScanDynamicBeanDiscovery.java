package br.com.cd.scaleframework.beans.dynamic.factory.support;

import java.util.ArrayList;
import java.util.Collection;

import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBean;
import br.com.cd.scaleframework.beans.dynamic.factory.DynamicBeanDiscovery;
import br.com.cd.scaleframework.controller.dynamic.BeanConfig;
import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;
import br.com.cd.scaleframework.controller.dynamic.CrudControllerBeanConfig;
import br.com.cd.scaleframework.core.orm.dynamic.ServiceBeanConfig;
import br.com.cd.scaleframework.web.controller.dynamic.WebControllerBeanConfig;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;

public class AnnotatedScanDynamicBeanDiscovery implements DynamicBeanDiscovery {

	private String packageToScan;

	public AnnotatedScanDynamicBeanDiscovery(String packageToScan) {
		this.packageToScan = packageToScan;
	}

	@Override
	public Collection<DynamicBean<? extends BeanConfig<?, ?>>> getCandidates() {

		Collection<DynamicBean<? extends BeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<? extends BeanConfig<?, ?>>>();

		beans.addAll(this.getServiceCandidates());
		beans.addAll(this.getControllerCandidates());
		beans.addAll(this.getCrudControllerCandidates());
		beans.addAll(this.getWebControllerCandidates());
		beans.addAll(this.getWebCrudControllerCandidates());
		return beans;
	}

	private Collection<DynamicBean<ServiceBeanConfig<?, ?>>> getServiceCandidates() {

		Collection<DynamicBean<ServiceBeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<ServiceBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBean<ControllerBeanConfig<?, ?>>> getControllerCandidates() {

		Collection<DynamicBean<ControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<ControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBean<CrudControllerBeanConfig<?, ?>>> getCrudControllerCandidates() {

		Collection<DynamicBean<CrudControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<CrudControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBean<WebControllerBeanConfig<?, ?>>> getWebControllerCandidates() {

		Collection<DynamicBean<WebControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<WebControllerBeanConfig<?, ?>>>();

		return beans;
	}

	private Collection<DynamicBean<WebCrudControllerBeanConfig<?, ?>>> getWebCrudControllerCandidates() {

		Collection<DynamicBean<WebCrudControllerBeanConfig<?, ?>>> beans = new ArrayList<DynamicBean<WebCrudControllerBeanConfig<?, ?>>>();

		return beans;
	}

}
