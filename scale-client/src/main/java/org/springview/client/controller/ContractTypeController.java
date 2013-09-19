package org.springview.client.controller;

import org.springview.client.model.ContractType;

import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.core.orm.dynamic.WebCrudControllerBean;

@WebCrudControllerBean(name = "contractTypeBean", path = "contractType", targetEntity = ContractType.class, entityIdType = Integer.class)
public class ContractTypeController {

	public ContractTypeController(Controller<ContractType, Integer> controller) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', controller: " + controller);

	}
}