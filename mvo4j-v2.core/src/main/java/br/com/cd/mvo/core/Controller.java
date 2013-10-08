package br.com.cd.mvo.core;

import java.io.Serializable;
import java.util.List;

import br.com.cd.mvo.Application;
import br.com.cd.mvo.Application.MessageLevel;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.bean.config.ControllerMetaData;

public interface Controller<T> extends PageableController, BeanObject {

	CrudService<T> getService();

	void setService(CrudService<T> Service);

	void addListener(ControllerListener<T> listener);

	void create();

	T newEntity() throws InstantiationException, IllegalAccessException;

	void save();

	void save(T entity);

	void update();

	void update(T entity);

	void delete();

	void delete(T entity);

	void delete(List<T> entity);

	void refreshList();

	T getCurrentEntity();

	void setCurrentEntity(T currentEntity);

	void setCurrentEntity(Serializable entityId);

	List<T> getEntityList();

	void setEntityList(List<T> entityList);

	DataModel<T> getDataModel();

	String getName();

	String getNameAsList();

	Application getApplication();

	Translator getTranslator();

	FilterManager getFilter();

	void addTranslatedMessage(MessageLevel level, String msgSumary,
			String msgDetail, Object... args);

	ControllerMetaData getControllerConfig();
}