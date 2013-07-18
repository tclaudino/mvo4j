package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.core.proxy.ServiceProxy;

@SuppressWarnings("rawtypes")
public interface ServiceComponentFactory extends
		ComponentListableFactory<ServiceComponent, ServiceProxy, ServiceBean> {

}
