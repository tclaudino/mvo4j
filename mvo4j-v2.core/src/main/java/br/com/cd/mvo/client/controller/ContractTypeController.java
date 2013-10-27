package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.web.bean.WebControllerBean;

@WebControllerBean(name = "contractTypeBean", path = "contractType", targetEntity = ContractType.class, entityIdType = Integer.class)
public class ContractTypeController {

	public ContractTypeController(Controller<ContractType> controller) {

		System.out.println(this.getClass().getName() + ".<init>");
	}
}