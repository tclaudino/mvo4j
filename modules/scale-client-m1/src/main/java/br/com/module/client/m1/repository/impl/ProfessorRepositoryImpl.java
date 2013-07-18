package br.com.module.client.m1.repository.impl;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import br.com.module.client.m1.model.ProfessorEntity;
import br.com.module.client.m1.repository.ProfessorRepository;

@Repository
public class ProfessorRepositoryImpl extends HibernateDaoSupport implements
		ProfessorRepository {

	@Autowired
	public ProfessorRepositoryImpl(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Collection<ProfessorEntity> list() {
		return super.getHibernateTemplate().loadAll(ProfessorEntity.class);
	}

	@Override
	public ProfessorEntity read(Integer id) {
		return super.getHibernateTemplate().load(ProfessorEntity.class, id);
	}

	@Override
	public void save(ProfessorEntity professor) {
		super.getHibernateTemplate().saveOrUpdate(professor);
	}

	@Override
	public void delete(ProfessorEntity professor) {
		super.getHibernateTemplate().delete(professor);
	}

}
