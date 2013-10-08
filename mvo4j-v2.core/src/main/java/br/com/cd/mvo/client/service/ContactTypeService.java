package br.com.cd.mvo.client.service;

import java.util.AbstractMap;
import java.util.Map.Entry;

import br.com.cd.mvo.bean.ServiceBean;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.ServiceListener;
import br.com.cd.mvo.orm.LikeCritirion;
import br.com.cd.mvo.orm.LikeCritirionEnum;

@ServiceBean(targetEntity = ContactType.class, name = "contactTypeService")
public class ContactTypeService implements ServiceListener<ContactType> {

	private ContactTypeRepository repository;
	private CrudService<ContactType> service;

	public ContactTypeService(ContactTypeRepository repository) {
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', repository: " + repository);

		this.repository = repository;
	}

	public ContactType testFindLike(
			@LikeCritirion(LikeCritirionEnum.ALL) Integer v1, String v2) {

		Entry<String, Object> entry1 = new AbstractMap.SimpleEntry<String, Object>(
				"", v1);

		@LikeCritirion(LikeCritirionEnum.END)
		Entry<String, Object> entry2 = new AbstractMap.SimpleEntry<String, Object>(
				"", v2);

		return service.getRepository().find(entry1, entry2);
	}

	public ContactType testLocalRepository(Integer v1) {

		return repository.testLocalRepository(v1);
	}

	@Override
	public void postConstruct(CrudService<ContactType> service) {
		this.service = service;
	}

	@Override
	public void preDestroy(CrudService<ContactType> service) {
	}
}