package org.springview.client.controller;

import org.springview.client.model.ContractType;

import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.EventListener;
import br.com.cd.scaleframework.controller.EventType;
import br.com.cd.scaleframework.orm.Service;

public class ContractTypeListener implements EventListener<ContractType> {

	public ContractTypeListener(Service<ContractType, Integer> service,
			Translator translator, Messenger messenger) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', service:  " + service
				+ "', translator:  " + translator + "', messenger:  "
				+ messenger);
	}

	@Override
	public boolean before(EventType event, ContractType entity,
			Messenger messenger, Translator translator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void post(EventType event, ContractType entity, Messenger messenger,
			Translator translator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(EventType event, ContractType entity,
			Messenger messenger, Translator translator, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postConstruct(Controller controler) {

		System.out.println(this.getClass().getName()
				+ ".postConstruct, controller: " + controler);
	}

	@Override
	public void preDestroy(Controller controler) {
		// TODO Auto-generated method stub

	}

}