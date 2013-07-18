package br.com.cd.scaleframework.orm;

import java.io.Serializable;

public interface Service<T, ID extends Serializable> extends Repository<T, ID> {

	Repository<T, ID> getRepository();
}