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

	private Controller<ContactType> controler;

	public ContactTypeControllerListener(ContactTypeService service) {

		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + ", service:  " + service);
	}

	@Override
	public boolean beforePersist(PersistEventType event, ContactType entity,
			Application application) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postPersist(PersistEventType event, ContactType entity,
			Application application) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPersistError(PersistEventType event, ContactType entity,
			Application application, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postConstruct(Controller<ContactType> controler) {

		this.controler = controler;
	}

	@Override
	public void preDestroy(Controller<ContactType> controler) {
		// TODO Auto-generated method stub

	}

}