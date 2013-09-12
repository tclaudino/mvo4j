package br.com.cd.scaleframework.web.controller;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.Controller;

public interface WebController<T, ID extends Serializable> extends
		Controller<T, ID> {

}