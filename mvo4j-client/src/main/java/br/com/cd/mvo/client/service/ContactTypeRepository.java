package br.com.cd.mvo.client.service;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map.Entry;

import br.com.cd.mvo.RepositoryBean;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.orm.LikeCritirion;
import br.com.cd.mvo.orm.LikeCritirionEnum;
import br.com.cd.mvo.orm.Repository;
import br.com.cd.mvo.orm.RepositoryListener;

@RepositoryBean(targetEntity = ContactType.class, name = "contactTypeRepository", entityIdType = Integer.class)
public class ContactTypeRepository implements RepositoryListener<ContactType> {

	public Repository<ContactType, ?> repositoryFromCtor;
	public Repository<ContactType, ?> repository;

	public ContactTypeRepository(Repository<ContactType, ?> repository) {

		System.out.println(this.getClass().getName() + ".<init>");
		this.repositoryFromCtor = repository;
	}

	public ContactType testLocalRepository(@LikeCritirion(LikeCritirionEnum.iSTART) String v1) {

		System.out.println(this.getClass().getName() + ".testLocalRepository");

		Entry<String, Object> entry1 = new AbstractMap.SimpleEntry<String, Object>("type", v1);

		return repository.find(entry1);
	}

	@Override
	public void onRead(Collection<ContactType> entity) {

		System.out.println(this.getClass().getName() + ".onRead");
	}

	@Override
	public boolean onSave(ContactType entity, ActionListenerEventType eventType) {

		System.out.println(this.getClass().getName() + ".onSave");
		return true;
	}

	@Override
	public boolean onUpdate(ContactType entity, ActionListenerEventType eventType) {

		System.out.println(this.getClass().getName() + ".onUpdate");
		return true;
	}

	@Override
	public boolean onDelete(ContactType entity, ActionListenerEventType eventType) {

		System.out.println(this.getClass().getName() + ".onDelete");
		return true;
	}

	@Override
	public void postConstruct(Repository<ContactType, ?> repository) {

		System.out.println(this.getClass().getName() + ".postConstruct -> repository.equals(repository from constructor)? \n'"
				+ repository.equals(repositoryFromCtor) + "'");
		this.repository = repository;
	}

	@Override
	public void preDestroy(Repository<ContactType, ?> repository) {

		System.out.println(this.getClass().getName() + ".preDestroy");
	}

}