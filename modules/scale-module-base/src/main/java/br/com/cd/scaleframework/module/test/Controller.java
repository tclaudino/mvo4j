package br.com.cd.scaleframework.module.test;

import java.io.Serializable;

public interface Controller<T, ID extends Serializable> {

	void test();

	void test(T object);

	void setPageNumber(Integer pageNumber);

	void setPageSize(Integer pageSize);

	void save(T model);

	void toUpdateMode();

	void setCurrentEntity(ID id);
}
