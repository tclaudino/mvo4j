package br.com.cd.scaleframework.core.orm;

import java.sql.SQLException;

import javax.persistence.EntityManager;

public interface Action<T> {

    T execute(EntityManager em) throws SQLException;
}
