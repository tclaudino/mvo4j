package br.com.cd.scaleframework.orm.hibernate;

import java.sql.SQLException;

import org.hibernate.Session;

public interface Action<T> {

	T execute(Session session) throws SQLException;
}