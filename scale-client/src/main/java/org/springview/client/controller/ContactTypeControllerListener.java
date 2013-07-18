package org.springview.client.controller;

import org.springview.client.model.ContactType;
import org.springview.client.service.ContactTypeService;

import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.EventListener;
import br.com.cd.scaleframework.controller.EventType;
import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;

@WebCrudControllerBean(name = "contactTypeBean", path = "contactType", targetEntity = ContactType.class, entityIdType = Integer.class)
public class ContactTypeControllerListener implements
		EventListener<ContactType> {

	public ContactTypeControllerListener(
			Controller<ContactType, Integer> controller,
			ContactTypeService service) {

		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', controller: " + controller
				+ ", service:  " + service);

	}

	@Override
	public boolean before(EventType event, ContactType entity,
			Messenger messenger, Translator translator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void post(EventType event, ContactType entity, Messenger messenger,
			Translator translator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(EventType event, ContactType entity, Messenger messenger,
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