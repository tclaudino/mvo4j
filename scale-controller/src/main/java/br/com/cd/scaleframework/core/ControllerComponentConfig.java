package br.com.cd.scaleframework.core;

import java.util.List;

import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.EventListener;

public interface ControllerComponentConfig<C extends Controller<?, ?>> extends
		ComponentConfig {

	public static final int INITIAL_PAGE_SIZE = 10;

	int getInitialPageSize();

	void setInitialPageSize(int initialPageSize);

	List<Class<? extends EventListener<C>>> getListeners();

	void setListeners(List<Class<? extends EventListener<C>>> listeners);

	void addListener(Class<? extends EventListener<C>> listernerClass);

	String getMessageBundle();

	void setMessageBundle(String messageBundle);

}