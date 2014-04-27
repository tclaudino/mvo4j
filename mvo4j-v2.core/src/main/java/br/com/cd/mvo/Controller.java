package br.com.cd.mvo;

import java.io.Serializable;
import java.util.Collection;

import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.core.BeanObject;
import br.com.cd.mvo.core.DataModel;
import br.com.cd.mvo.core.PageableController;

public interface Controller<T> extends PageableController, BeanObject<T> {

	CrudService<T> getService();

	void setService(CrudService<T> Service);

	void create();

	T newEntity() throws InstantiationException, IllegalAccessException;

	void save();

	void save(T entity);

	void update();

	void update(T entity);

	void delete();

	void delete(T entity);

	void delete(Collection<T> entity);

	void refreshList();

	T getCurrentEntity();

	void setCurrentEntity(T currentEntity);

	void setCurrentEntity(Serializable entityId);

	Collection<T> getEntityList();

	void setEntityList(Collection<T> entityList);

	DataModel<T> getDataModel();

	String getName();

	String getNameAsList();

	Application getApplication();

	Translator getTranslator();

	FilterManager getFilter();

	void addTranslatedMessage(MessageLevel level, String msgSumary, String msgDetail, Object... args);
}