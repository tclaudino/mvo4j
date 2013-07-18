package br.com.module.client.m1.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.module.client.m1.model.ProfessorEntity;
import br.com.module.client.m1.repository.ProfessorRepository;
import br.com.module.client.m1.service.ProfessorService;

@Service
public class ProfessorServiceImpl implements ProfessorService {

	private ProfessorRepository repository;

	@Autowired
	public ProfessorServiceImpl(ProfessorRepository repository) {
		this.repository = repository;
	}

	@Override
	public Collection<ProfessorEntity> list() {
		return repository.list();
	}

	@Override
	public ProfessorEntity read(Integer id) {
		return repository.read(id);
	}

	@Override
	public void save(ProfessorEntity professor) {
		this.repository.save(professor);
	}

	@Override
	public void delete(ProfessorEntity professor) {
		this.repository.delete(professor);
	}

}
