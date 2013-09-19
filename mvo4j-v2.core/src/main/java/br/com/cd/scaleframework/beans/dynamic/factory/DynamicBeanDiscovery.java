package br.com.cd.scaleframework.beans.dynamic.factory;

import java.util.Collection;

public interface DynamicBeanDiscovery {

	Collection<DynamicBeanManager<?>> getCandidates();
}