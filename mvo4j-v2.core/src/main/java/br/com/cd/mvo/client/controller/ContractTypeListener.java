package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.PersistEventType;
import br.com.cd.mvo.orm.Repository;

public class ContractTypeListener implements ControllerListener<ContractType> {

	public ContractTypeListener(Repository<ContractType> service, Translator translator, Application application) {

		System.out.println(this.getClass().getName() + ".<init>");
	}

	@Override
	public boolean beforePersist(PersistEventType event, ContractType entity, Application application) {

		System.out.println(this.getClass().getName() + ".beforePersist");
		return true;
	}

	@Override
	public void postPersist(PersistEventType event, ContractType entity, Application application) {

		System.out.println(this.getClass().getName() + ".postPersist");
	}

	@Override
	public void onPersistError(PersistEventType event, ContractType entity, Application application, Throwable t) {

		System.out.println(this.getClass().getName() + ".onPersistError");
	}

	@Override
	public void postConstruct(Controller<ContractType> controler) {

		System.out.println(this.getClass().getName() + ".postConstruct -> is ContractTypeController? '"
				+ ContractTypeController.class.isAssignableFrom(controler.getClass()) + "'");
	}

	@Override
	public void preDestroy(Controller<ContractType> controler) {

		System.out.println(this.getClass().getName() + ".preDestroy");
	}

}