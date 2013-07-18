package org.springview.client.service;

import org.springview.client.model.ContactType;
import org.springview.client.repository.ContactTypeRepository;

import br.com.cd.scaleframework.core.dynamic.ServiceBean;

@ServiceBean(targetEntity = ContactType.class, name = "contactTypeService")
public class ContactTypeService {

	public ContactTypeService(ContactTypeRepository repository) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', repository: " + repository);

	}
}