package br.com.cd.scaleframework.core.proxy;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.orm.suport.ServiceFacade;

@SuppressWarnings("rawtypes")
public interface ControllerProxy<O extends ControllerComponent> extends
		Controller<Object, Serializable>, ComponentProxy<O> {

	@SuppressWarnings("unchecked")
	ServiceFacade getService();

	void setService(ServiceFacade service);

}