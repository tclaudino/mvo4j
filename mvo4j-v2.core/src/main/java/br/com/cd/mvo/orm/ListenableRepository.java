package br.com.cd.mvo.orm;


public interface ListenableRepository<T> extends Repository<T> {

	RepositoryListener<T> getListener();

	void setListener(final RepositoryListener<T> listener);

	void afterPropertiesSet();

	void destroy();

}
