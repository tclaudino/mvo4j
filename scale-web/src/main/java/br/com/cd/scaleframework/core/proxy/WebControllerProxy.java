package br.com.cd.scaleframework.core.proxy;

import java.io.Serializable;

import br.com.cd.scaleframework.core.WebControllerComponent;
import br.com.cd.scaleframework.web.controller.WebController;

public interface WebControllerProxy extends
		ControllerProxy<WebControllerComponent>,
		WebController<Object, Serializable> {

}
