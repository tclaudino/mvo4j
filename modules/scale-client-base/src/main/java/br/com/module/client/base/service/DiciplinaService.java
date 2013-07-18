package br.com.module.client.base.service;

import java.util.Collection;

import br.com.module.client.base.model.DiciplinaEntity;

public interface DiciplinaService {

	Collection<DiciplinaEntity> list();

	DiciplinaEntity read(Integer id);

	void delete(DiciplinaEntity diciplina);

	void save(DiciplinaEntity diciplina);
}
