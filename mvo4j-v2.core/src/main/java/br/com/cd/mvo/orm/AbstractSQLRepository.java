package br.com.cd.mvo.orm;

import br.com.cd.mvo.core.RepositoryMetaData;

public abstract class AbstractSQLRepository<T, D> extends AbstractRepository<T, D> implements SQLRepository<T, D> {

	public AbstractSQLRepository(RepositoryMetaData<T> metaData) {
		super(metaData);
	}

}
