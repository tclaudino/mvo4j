package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.web.controller.WebController;

public class WebControllerComponentConfig extends
		AbstractControllerComponentConfig<WebController<?, ?>> {

	private String path;

	public WebControllerComponentConfig(String name, String path,
			String targetEntityClassName) {
		super(name, targetEntityClassName);
		this.path = path;
	}

	public WebControllerComponentConfig(String name, String path,
			Class<?> targetEntity) {
		super(name, targetEntity);
		this.path = path;
	}

	public WebControllerComponentConfig(String name, String path,
			String targetEntityClassName, int initialPageSize) {
		super(name, targetEntityClassName, initialPageSize);
		this.path = path;
	}

	public WebControllerComponentConfig(String name, String path,
			Class<?> targetEntity, int initialPageSize) {
		super(name, targetEntity, initialPageSize);
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getBeanName() {
		return path;
	}

}