package br.com.module.client.m1.repository;

import java.util.Collection;

import br.com.module.client.m1.model.ProfessorEntity;

public interface ProfessorRepository {

	Collection<ProfessorEntity> list();

	ProfessorEntity read(Integer id);

	void delete(ProfessorEntity professor);

	void save(ProfessorEntity professor);
}
