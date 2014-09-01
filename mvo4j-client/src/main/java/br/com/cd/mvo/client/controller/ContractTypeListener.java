package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ControllerListener;
import br.com.cd.mvo.PersistEventType;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.web.WebController;

public class ContractTypeListener implements ControllerListener<ContractType> {

	private WebController<ContractType> controllerFromCtor;

	public ContractTypeListener(WebController<ContractType> controller, Repository<ContractType, ?> service, Translator translator,
			Application application) {

		this.controllerFromCtor = controller;
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
				+ ContractTypeController.class.isAssignableFrom(controler.getClass())
				+ "' -> repository.equals(repository from constructor)? '" + controler.equals(controllerFromCtor) + "'");
	}

	@Override
	public void preDestroy(Controller<ContractType> controler) {

		System.out.println(this.getClass().getName() + ".preDestroy");
	}

}