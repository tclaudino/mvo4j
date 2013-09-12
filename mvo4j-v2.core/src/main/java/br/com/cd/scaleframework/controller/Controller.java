package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.List;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.Application.MessageLevel;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;
import br.com.cd.scaleframework.core.orm.Service;

public interface Controller<T, ID extends Serializable> extends
		PageableController {

	Service<T, ID> getService();

	void setService(Service<T, ID> Service);

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

	void setCurrentEntity(ID entityId);

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

	ControllerBeanConfig<T, ID> getControllerConfig();
}