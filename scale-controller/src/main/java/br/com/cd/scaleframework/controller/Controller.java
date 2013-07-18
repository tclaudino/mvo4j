package br.com.cd.scaleframework.controller;

import java.io.Serializable;
import java.util.List;

import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.AbstractController.MessageLevel;
import br.com.cd.scaleframework.controller.suport.Pageable;
import br.com.cd.scaleframework.orm.Service;

public interface Controller<T, ID extends Serializable> extends Pageable {

	<TD extends Service<T, ID>> TD getService();

	void addListener(EventListener<T> listener);

	void create();

	T newEntity();

	void save();

	void save(T entity);

	void update();

	void update(T entity);

	void delete();

	void delete(T entity);

	void delete(List<T> entity);

	void resetList();

	T getCurrentEntity();

	void setCurrentEntity(T currentEntity);

	void setCurrentEntity(ID entityId);

	List<T> getEntityList();

	void setEntityList(List<T> entityList);

	DataModel<T> getDataModel();

	String getName();

	String getNameAsList();

	Messenger getMessenger();

	Translator getTranslator();

	void addMessage(MessageLevel level, String msgSumary, String msgDetail,
			Object... args);

}
