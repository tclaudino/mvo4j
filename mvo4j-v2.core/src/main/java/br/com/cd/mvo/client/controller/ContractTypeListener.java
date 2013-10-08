package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.PersistEventType;
import br.com.cd.mvo.orm.Repository;

public class ContractTypeListener implements ControllerListener<ContractType> {

	public ContractTypeListener(Repository<ContractType> service,
			Translator translator, Application messenger) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', service:  " + service
				+ "', translator:  " + translator + "', messenger:  "
				+ messenger);
	}

	@Override
	public boolean beforePersist(PersistEventType event, ContractType entity,
			Application application) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postPersist(PersistEventType event, ContractType entity,
			Application application) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPersistError(PersistEventType event, ContractType entity,
			Application application, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postConstruct(Controller<ContractType> controler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDestroy(Controller<ContractType> controler) {
		// TODO Auto-generated method stub

	}

}