package br.com.cd.scaleframework.core.proxy;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.CrudController;
import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.orm.suport.ServiceFacade;

@SuppressWarnings("rawtypes")
public interface CrudControllerProxy<T extends ControllerComponent> extends
		ControllerProxy<T>, CrudController<Object, Serializable>,
		ComponentProxy<T> {

	@SuppressWarnings("unchecked")
	ServiceFacade getService();

	void setService(ServiceFacade service);

}
