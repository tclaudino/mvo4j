package br.com.cd.scaleframework.jpa;

import java.sql.SQLException;

import javax.persistence.EntityManager;

public interface ActionWithoutResult {

	void execute(EntityManager em) throws SQLException;
}
