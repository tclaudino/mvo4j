package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.web.WebController;
import br.com.cd.mvo.web.WebControllerBean;

@WebControllerBean(name = "contractTypeBean", path = "contractType", targetEntity = ContractType.class, entityIdType = Integer.class)
public class ContractTypeController {

	private WebController<ContractType> controller;

	public ContractTypeController(WebController<ContractType> controller) {

		System.out.println(this.getClass().getName() + ".<init> --> is ContractTypeController(this) ? '"
				+ ContractTypeController.class.isAssignableFrom(controller.getClass()) + "'");
		this.controller = controller;
	}
}