package br.com.cd.mvo.client.service;

import java.util.AbstractMap;
import java.util.Map.Entry;

import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.CrudServiceListener;
import br.com.cd.mvo.ServiceBean;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.orm.LikeCritirion;
import br.com.cd.mvo.orm.LikeCritirionEnum;

@ServiceBean(targetEntity = ContactType.class, name = "contactTypeService", entityIdType = Integer.class)
public class ContactTypeService implements CrudServiceListener<ContactType> {

	private ContactTypeRepository repository;
	private CrudService<ContactType> serviceFromCtor;
	private CrudService<ContactType> service;

	public ContactTypeService(CrudService<ContactType> service, ContactTypeRepository repository) {

		System.out.println(this.getClass().getName() + ".<init>");
		this.serviceFromCtor = service;
		this.repository = repository;
	}

	public ContactType testFindLike(@LikeCritirion(LikeCritirionEnum.ALL) String v1, @LikeCritirion(LikeCritirionEnum.iSTART) String v2) {

		System.out.println(this.getClass().getName() + ".testFindLike");

		Entry<String, Object> entry1 = new AbstractMap.SimpleEntry<String, Object>("type", v1);

		Entry<String, Object> entry2 = new AbstractMap.SimpleEntry<String, Object>("acronym", v2);

		return service.getRepository().find(entry1, entry2);
	}

	public ContactType testLocalRepository(String v1) {

		System.out.println(this.getClass().getName() + ".testLocalRepository");

		return repository.testLocalRepository(v1);
	}

	@Override
	public void postConstruct(CrudService<ContactType> service) {

		System.out.println(this.getClass().getName() + ".postConstruct -> repository.equals(repository from constructor)? '"
				+ service.equals(serviceFromCtor) + "'");
		this.service = service;
	}

	@Override
	public void preDestroy(CrudService<ContactType> service) {

		System.out.println(this.getClass().getName() + ".preDestroy");
	}
}