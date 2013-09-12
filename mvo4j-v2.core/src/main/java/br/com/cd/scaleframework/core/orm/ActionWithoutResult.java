package br.com.cd.scaleframework.core.orm;

import java.sql.SQLException;

import javax.persistence.EntityManager;

public interface ActionWithoutResult {

	void execute(EntityManager em) throws SQLException;
}
