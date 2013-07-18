package br.com.cd.scaleframework.core.support;

import java.util.LinkedList;
import java.util.List;

import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.controller.EventListener;
import br.com.cd.scaleframework.core.ControllerComponentConfig;

public abstract class AbstractControllerComponentConfig<C extends Controller<?, ?>>
		extends AbstractComponentConfig implements ControllerComponentConfig<C> {

	private List<Class<? extends EventListener<C>>> listeners = new LinkedList<Class<? extends EventListener<C>>>();

	private String messageBundle = "";

	private int initialPageSize = -1;

	private Class<?> targetService;

	public AbstractControllerComponentConfig(String name,
			String targetEntityClassName) {
		super(name, targetEntityClassName);
	}

	public AbstractControllerComponentConfig(String name,
			String targetEntityClassName, int initialPageSize) {
		this(name, targetEntityClassName);
		this.initialPageSize = initialPageSize;
	}

	public AbstractControllerComponentConfig(String name, Class<?> targetEntity) {
		super(name, targetEntity);
	}

	public AbstractControllerComponentConfig(String name,
			Class<?> targetEntity, int initialPageSize) {
		this(name, targetEntity);
		this.initialPageSize = initialPageSize;
	}

	public int getInitialPageSize() {
		return initialPageSize;
	}

	public void setInitialPageSize(int initialPageSize) {
		this.initialPageSize = initialPageSize;
	}

	public Class<?> getTargetService() {
		return targetService;
	}

	public void setTargetService(Class<?> targetService) {
		this.targetService = targetService;
	}

	public List<Class<? extends EventListener<C>>> getListeners() {
		return listeners;
	}

	public void setListeners(List<Class<? extends EventListener<C>>> listeners) {
		this.listeners = listeners;
	}

	public void addListener(Class<? extends EventListener<C>> listernerClass) {
		this.listeners.add(listernerClass);
	}

	public String getMessageBundle() {
		return messageBundle;
	}

	public void setMessageBundle(String messageBundle) {
		this.messageBundle = messageBundle;
	}

	public String getBeanName() {
		return getName();
	}
}