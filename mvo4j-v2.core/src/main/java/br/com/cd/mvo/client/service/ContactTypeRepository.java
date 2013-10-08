package br.com.cd.mvo.client.service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.cd.mvo.bean.RepositoryBean;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.core.RepositoryListener;
import br.com.cd.mvo.orm.LikeCritirion;
import br.com.cd.mvo.orm.LikeCritirionEnum;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.Repository.ActionListenerEventType;

@RepositoryBean(targetEntity = ContactType.class, name = "contactTypeService")
public class ContactTypeRepository implements RepositoryListener<ContactType> {

	public Repository<ContactType> repository;

	public ContactType testLocalRepository(Integer v1) {

		@LikeCritirion(LikeCritirionEnum.END)
		Entry<String, Object> entry1 = new AbstractMap.SimpleEntry<String, Object>(
				"", v1);

		return repository.find(entry1);
	}

	@Override
	public void onRead(List<ContactType> entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(ContactType entity, ActionListenerEventType eventType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUpdate(ContactType entity,
			ActionListenerEventType eventType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDelete(ContactType entity,
			ActionListenerEventType eventType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postConstruct(Repository<ContactType> repository) {
		this.repository = repository;
	}

	@Override
	public void preDestroy(Repository<ContactType> repository) {
		// TODO Auto-generated method stub

	}

}