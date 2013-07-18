package br.com.module.client.base.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.module.client.base.model.DiciplinaEntity;
import br.com.module.client.base.repository.DiciplinaRepository;
import br.com.module.client.base.service.DiciplinaService;

@Service
public class DiciplinaServiceImpl implements DiciplinaService {

	private DiciplinaRepository repository;

	@Autowired
	public DiciplinaServiceImpl(DiciplinaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Collection<DiciplinaEntity> list() {
		return repository.list();
	}

	@Override
	public DiciplinaEntity read(Integer id) {
		return repository.read(id);
	}

	@Override
	public void delete(DiciplinaEntity diciplina) {
		this.repository.delete(diciplina);
	}

	@Override
	public void save(DiciplinaEntity diciplina) {
		this.repository.save(diciplina);
	}

}
