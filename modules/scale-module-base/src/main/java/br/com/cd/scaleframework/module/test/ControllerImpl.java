package br.com.cd.scaleframework.module.test;

import java.io.Serializable;

public class ControllerImpl<T, ID extends Serializable> implements
		Controller<T, ID> {

	private Class<T> type;
	private Class<ID> idType;

	public ControllerImpl(Class<T> type, Class<ID> idType) {
		this.type = type;
		this.idType = idType;
	}

	public void test() {
		System.out.println("Generic Type is: " + type.getName());
	}

	public void test(T object) {
		System.out.println("Generic Type is: " + object.getClass().getName());
	}

	@Override
	public void setPageNumber(Integer pageNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPageSize(Integer pageSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(T model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toUpdateMode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCurrentEntity(ID id) {
		// TODO Auto-generated method stub

	}
}
