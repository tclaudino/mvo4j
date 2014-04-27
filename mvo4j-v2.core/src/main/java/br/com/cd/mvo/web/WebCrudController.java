package br.com.cd.mvo.web;

import br.com.cd.mvo.CrudController;

public interface WebCrudController<T> extends WebController<T>, CrudController<T> {

	String getViewsFolder();

	String getListView();

	String getEditView();

	String getCreateView();

}