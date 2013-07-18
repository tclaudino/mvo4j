package br.com.cd.scaleframework.jpa;

import java.sql.SQLException;

import javax.persistence.EntityManager;

public interface Action<T> {

    T execute(EntityManager em) throws SQLException;
}
