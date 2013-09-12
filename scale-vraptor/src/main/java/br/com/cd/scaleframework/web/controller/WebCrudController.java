package br.com.cd.scaleframework.web.controller;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.CrudController;

public interface WebCrudController<T, ID extends Serializable> extends
		WebController<T, ID>, CrudController<T, ID> {

	String getViewsFolder();

	String getListView();

	String getEditView();

	String getCreateView();

}