package org.springview.client.controller;

import org.springview.client.model.ContactType;
import org.springview.client.service.ContactTypeService;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.ControllerListener;
import br.com.cd.scaleframework.controller.PersistEventType;
import br.com.cd.scaleframework.core.orm.dynamic.WebCrudControllerBean;

@WebCrudControllerBean(name = "contactTypeBean", path = "contactType", targetEntity = ContactType.class, entityIdType = Integer.class)
public class ContactTypeControllerListener implements
		ControllerListener<ContactType> {

	public ContactTypeControllerListener(
			Controller<ContactType, Integer> controller,
			ContactTypeService service) {

		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', controller: " + controller
				+ ", service:  " + service);

	}

	@Override
	public boolean beforePersist(PersistEventType event, ContactType entity,
			Application messenger, Translator translator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void post(PersistEventType event, ContactType entity, Application messenger,
			Translator translator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(PersistEventType event, ContactType entity, Application messenger,
			Translator translator, Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postConstruct(Controller controler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preDestroy(Controller controler) {
		// TODO Auto-generated method stub

	}
}