package br.com.cd.scaleframework.orm.hibernate;

import java.sql.SQLException;

import org.hibernate.Session;

public interface ActionWithoutResult {

	void execute(Session session) throws SQLException;
}