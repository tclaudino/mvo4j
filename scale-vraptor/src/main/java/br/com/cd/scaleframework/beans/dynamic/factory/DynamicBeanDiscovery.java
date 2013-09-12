package br.com.cd.scaleframework.beans.dynamic.factory;

import java.util.Collection;

import br.com.cd.scaleframework.controller.dynamic.BeanConfig;

public interface DynamicBeanDiscovery {

	Collection<DynamicBean<? extends BeanConfig<?, ?>>> getCandidates();
}