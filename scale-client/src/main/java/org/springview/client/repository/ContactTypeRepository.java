package org.springview.client.repository;

import org.hibernate.SessionFactory;
import org.springview.client.model.ContactType;

import br.com.cd.scaleframework.core.orm.dynamic.RepositoryBean;

@RepositoryBean(targetEntity = ContactType.class, name = "contactTypeRepository")
public class ContactTypeRepository {

	public ContactTypeRepository(SessionFactory factory) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', factory: " + factory);
	}
}