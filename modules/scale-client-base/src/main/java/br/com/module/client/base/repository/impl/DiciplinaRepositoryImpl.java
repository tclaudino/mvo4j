package br.com.module.client.base.repository.impl;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import br.com.module.client.base.model.DiciplinaEntity;
import br.com.module.client.base.repository.DiciplinaRepository;

@Repository
public class DiciplinaRepositoryImpl extends HibernateDaoSupport implements
		DiciplinaRepository {

	@Autowired
	public DiciplinaRepositoryImpl(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Collection<DiciplinaEntity> list() {
		return super.getHibernateTemplate().loadAll(DiciplinaEntity.class);
	}

	@Override
	public DiciplinaEntity read(Integer id) {
		return super.getHibernateTemplate().load(DiciplinaEntity.class, id);
	}

	@Override
	public void delete(DiciplinaEntity diciplina) {
		super.getHibernateTemplate().delete(diciplina);
	}

	@Override
	public void save(DiciplinaEntity diciplina) {
		super.getHibernateTemplate().saveOrUpdate(diciplina);
	}

}
