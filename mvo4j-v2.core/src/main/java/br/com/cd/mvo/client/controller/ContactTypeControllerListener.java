package br.com.cd.mvo.client.controller;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.client.service.ContactTypeService;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.core.ControllerListener;
import br.com.cd.mvo.core.PersistEventType;
import br.com.cd.mvo.web.bean.WebControllerBean;

@WebControllerBean(name = "contactTypeBean", path = "contactType", targetEntity = ContactType.class, entityIdType = Integer.class)
public class ContactTypeControllerListener implements
		ControllerListener<ContactType> {

	private ContactTypeService service;
	private Controller<ContactType> controler;

	public ContactTypeControllerListener(ContactTypeService service) {

		System.out.println(this.getClass().getName() + ".<init>");
		this.service = service;
	}

	@Override
	public boolean beforePersist(PersistEventType event, ContactType entity,
			Application application) {

		System.out.println(this.getClass().getName() + ".beforePersist");
		return true;
	}

	@Override
	public void postPersist(PersistEventType event, ContactType entity,
			Application application) {

		System.out.println(this.getClass().getName() + ".postPersist");
	}

	@Override
	public void onPersistError(PersistEventType event, ContactType entity,
			Application application, Throwable t) {

		System.out.println(this.getClass().getName() + ".onPersistError");
	}

	@Override
	public void postConstruct(Controller<ContactType> controler) {

		System.out.println(this.getClass().getName() + ".postConstruct");
		this.controler = controler;
	}

	@Override
	public void preDestroy(Controller<ContactType> controler) {

		System.out.println(this.getClass().getName() + ".preDestroy");
	}

}