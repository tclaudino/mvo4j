package org.springview.client.controller;

import org.springview.client.model.ContractType;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.ControllerListener;
import br.com.cd.scaleframework.controller.PersistEventType;
import br.com.cd.scaleframework.core.orm.Service;

public class ContractTypeListener implements ControllerListener<ContractType> {

	public ContractTypeListener(Service<ContractType, Integer> service,
			Translator translator, Application messenger) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', service:  " + service
				+ "', translator:  " + translator + "', messenger:  "
				+ messenger);
	}

	@Override
	public boolean beforePersist(PersistEventType event, ContractType entity,
			Application messenger, Translator translator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void post(PersistEventType event, ContractType entity, Application messenger,
			Translator translator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(PersistEventType event, ContractType entity,
			Application messenger, Translator translator, Throwable t) {
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